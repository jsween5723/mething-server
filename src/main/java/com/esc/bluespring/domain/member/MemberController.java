package com.esc.bluespring.domain.member;

import static com.esc.bluespring.domain.member.entity.Member.ADMIN;
import static com.esc.bluespring.domain.member.entity.Member.STUDENT;

import com.esc.bluespring.common.BaseResponse;
import com.esc.bluespring.common.security.CustomJwtEncoder;
import com.esc.bluespring.common.utils.file.S3Service;
import com.esc.bluespring.domain.auth.exception.AuthException.LoginRequiredException;
import com.esc.bluespring.domain.file.entity.Image;
import com.esc.bluespring.domain.member.classes.MemberDto.AdminJoin;
import com.esc.bluespring.domain.member.classes.MemberDto.Join;
import com.esc.bluespring.domain.member.classes.MemberDto.JwtToken;
import com.esc.bluespring.domain.member.classes.MemberDto.Login;
import com.esc.bluespring.domain.member.classes.MemberDto.Patch;
import com.esc.bluespring.domain.member.classes.MemberDto.SendFriendShipRequest;
import com.esc.bluespring.domain.member.classes.MemberMapper;
import com.esc.bluespring.domain.member.entity.Admin;
import com.esc.bluespring.domain.member.entity.Member;
import com.esc.bluespring.domain.member.entity.RefreshToken;
import com.esc.bluespring.domain.member.entity.Student;
import com.esc.bluespring.domain.member.exception.MemberException.MemberNotFoundException;
import com.esc.bluespring.domain.member.student.classes.StudentDto.Detail;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.security.RolesAllowed;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/members")
public class MemberController {

    private final MemberServiceFacade memberServiceFacade;
    private final S3Service s3Service;
    private final MemberMapper mapper = MemberMapper.INSTANCE;
    private final CustomJwtEncoder customJwtEncoder;
    private final RefreshTokenService refreshTokenService;

    @PostMapping("join")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(description = "학생 가입 API")
    public BaseResponse<Boolean> join(Join dto) {
        Image profile = s3Service.upload(dto.profileImage());
        Image certificateImage = s3Service.upload(dto.studentCertificationImage());
        memberServiceFacade.join(mapper.toEntity(dto, profile, certificateImage));
        return new BaseResponse<>(true);
    }

    @PostMapping("admin-join")
    @ResponseStatus(HttpStatus.CREATED)
    public BaseResponse<Boolean> join(@RequestBody AdminJoin dto) {
        memberServiceFacade.join(mapper.toEntity(dto));
        return new BaseResponse<>(true);
    }

    @DeleteMapping("resign")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @RolesAllowed({ADMIN, STUDENT})
    @Operation(description = "회원 탈퇴")
    public BaseResponse<Boolean> resign(Member member) {
        if (member instanceof Student student) {
            s3Service.remove(student.getProfileImage().getUrl());
        }
        memberServiceFacade.resign(member);
        return new BaseResponse<>(true);
    }

    @PostMapping("{id}/friendship-requests")
    @ResponseStatus(HttpStatus.CREATED)
    @RolesAllowed({STUDENT})
    @Operation(description = "친구신청을 합니다.")
    public BaseResponse<Boolean> requestFriendship(Student user, @PathVariable UUID id,
                                                   @RequestBody SendFriendShipRequest dto) {
        try {
            Student target = (Student) memberServiceFacade.find(id);
            memberServiceFacade.requestFriendship(user, target, dto.message());
        } catch (ClassCastException e) {
            throw new MemberNotFoundException();
        }
        return new BaseResponse<>(true);
    }

    @PostMapping("login")
    @Operation(description = "로그인하면 쿠키에 리프레쉬 토큰을 저장하고 엑세스토큰을 바디로 반환합니다.")
    public BaseResponse<JwtToken> login(@RequestBody @Valid Login dto, HttpServletResponse response) {
        Member userDetails = memberServiceFacade.login(dto);
        Cookie refreshToken = new Cookie("refreshToken",
            refreshTokenService.create(userDetails).toString());
        refreshToken.isHttpOnly();
        response.addCookie(refreshToken);
        return new BaseResponse<>(new JwtToken(customJwtEncoder.generateAccessToken(userDetails)));
    }

    @GetMapping("refresh")
    @Operation(description = "새로운 엑세스 토큰을 발급받습니다. 로그인시 쿠키에 저장된 리프레쉬토큰과 서버에 저장된 정보를 대조하여 재발급합니다.")
    public BaseResponse<JwtToken> refresh(HttpServletRequest request, HttpServletResponse response) {
        RefreshToken refresh = refreshTokenService.refresh(getRefreshTokenFromCookie(request));
        Cookie refreshToken = new Cookie("refreshToken", refresh.getRefreshToken().toString());
        refreshToken.isHttpOnly();
        response.addCookie(refreshToken);
        return new BaseResponse<>(new JwtToken(customJwtEncoder.generateAccessToken(refresh.getMember())));
    }

    private UUID getRefreshTokenFromCookie(HttpServletRequest request) {
        if (request.getCookies() == null) {
            throw new LoginRequiredException();
        }
        List<Cookie> cookies = Arrays.stream(request.getCookies())
            .filter(cookie -> cookie.getName().equals("refreshToken")).toList();
        if (cookies.isEmpty() || cookies.get(0).getValue() == null || cookies.get(0).getValue()
            .isBlank()) {
            throw new LoginRequiredException();
        }
        return UUID.fromString(cookies.get(0).getValue());
    }

    @DeleteMapping("logout")
    @Operation(description = "서버에서 리프레쉬토큰을 지우고 쿠키에서 제거합니다.")
    public BaseResponse<Boolean> logout(HttpServletRequest request, HttpServletResponse response) {
        refreshTokenService.delete(getRefreshTokenFromCookie(request));
        Cookie refreshToken = new Cookie("refreshToken", null);
        refreshToken.isHttpOnly();
        response.addCookie(refreshToken);
        return new BaseResponse<>(true);
    }

    @PatchMapping("/me")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(description = "회원정보 변경 API(비밀번호, 이메일 변경시 이메일 인증 필수)")
    public BaseResponse<Boolean> patch(@RequestBody @Valid Patch dto, Member target) {
        if (target instanceof Admin) {
            Admin source = mapper.toEntity(dto);
            memberServiceFacade.patch(source, target);
        }
        if (target instanceof Student) {
            Image profile = s3Service.upload(dto.profileImage());
            Image certificateImage = s3Service.upload(dto.studentCertificationImage());
            Student source = mapper.toEntity(dto, profile, certificateImage);
            memberServiceFacade.patch(source, target);
        }
        return new BaseResponse<>(true);
    }

    @GetMapping("/me")
    @RolesAllowed({STUDENT})
    public BaseResponse<Detail> getMe(Student member) {
        return new BaseResponse<>(mapper.toDetail(member));
    }
}

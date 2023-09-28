package com.esc.bluespring.domain.member;

import static com.esc.bluespring.domain.member.entity.Member.ADMIN;
import static com.esc.bluespring.domain.member.entity.Member.STUDENT;

import com.esc.bluespring.common.security.CustomJwtEncoder;
import com.esc.bluespring.common.utils.file.S3Service;
import com.esc.bluespring.domain.file.entity.Image;
import com.esc.bluespring.domain.member.classes.AdminMapper;
import com.esc.bluespring.domain.member.classes.MemberDto.AdminJoin;
import com.esc.bluespring.domain.member.classes.MemberDto.Join;
import com.esc.bluespring.domain.member.classes.MemberDto.JwtToken;
import com.esc.bluespring.domain.member.classes.MemberDto.Login;
import com.esc.bluespring.domain.member.classes.MemberDto.Patch;
import com.esc.bluespring.domain.member.classes.MemberDto.SendFriendShipRequest;
import com.esc.bluespring.domain.member.classes.StudentMapper;
import com.esc.bluespring.domain.member.entity.Member;
import com.esc.bluespring.domain.member.entity.Student;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.security.RolesAllowed;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
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
    private final StudentMapper mapper = StudentMapper.INSTANCE;
    private final AdminMapper adminMapper = AdminMapper.INSTANCE;
    private final CustomJwtEncoder customJwtEncoder;

    @PostMapping("join")
    @ResponseStatus(HttpStatus.CREATED)
    public void join(Join dto) {
        Image profile = s3Service.upload(dto.profileImage());
        Image certificateImage = s3Service.upload(dto.studentCertificationImage());
        memberServiceFacade.join(mapper.toEntity(dto, profile, certificateImage));
    }

    @PostMapping("admin-join")
    @ResponseStatus(HttpStatus.CREATED)
    public void join(@RequestBody AdminJoin dto) {
        memberServiceFacade.join(adminMapper.toEntity(dto));
    }

    @DeleteMapping("resign")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @RolesAllowed({ADMIN, STUDENT})
    public void resign(Student student) {
        s3Service.remove(student.getProfileImage().getUrl());
        memberServiceFacade.resign(student);
    }

    @PostMapping("{id}/friendship-requests")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(description = "친구신청을 합니다.")
    public void requestFriendship(Member user, @PathVariable Long id,
        @RequestBody SendFriendShipRequest dto) {
        Member target = memberServiceFacade.find(id);
        memberServiceFacade.requestFriendship(user, target, dto.message());
    }

    @PostMapping("login")
    public JwtToken login(@RequestBody @Valid Login dto, HttpServletResponse response) {
        Member userDetails = memberServiceFacade.login(dto);
        Cookie refreshToken = new Cookie("refreshToken", customJwtEncoder.generateRefreshToken());
        refreshToken.isHttpOnly();
        refreshToken.setSecure(true);
        response.addCookie(refreshToken);
        return new JwtToken(customJwtEncoder.generateAccessToken(userDetails));
    }

    @PatchMapping("/me")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void patch(@RequestBody @Valid Patch dto, Student student) {
        Image profile = s3Service.upload(dto.profileImage());
        Image certificateImage = s3Service.upload(dto.studentCertificationImage());
        memberServiceFacade.patch(mapper.toEntity(dto, profile, certificateImage), student);
    }
}

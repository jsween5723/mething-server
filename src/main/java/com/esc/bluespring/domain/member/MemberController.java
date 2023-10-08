package com.esc.bluespring.domain.member;

import static com.esc.bluespring.domain.member.entity.Member.ADMIN;
import static com.esc.bluespring.domain.member.entity.Member.STUDENT;

import com.esc.bluespring.common.security.CustomJwtEncoder;
import com.esc.bluespring.common.utils.file.S3Service;
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
import com.esc.bluespring.domain.member.entity.Student;
import com.esc.bluespring.domain.member.exception.MemberException.MemberNotFoundException;
import com.esc.bluespring.domain.member.student.classes.StudentDto.Detail;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.security.RolesAllowed;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
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
        memberServiceFacade.join(mapper.toEntity(dto));
    }

    @DeleteMapping("resign")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @RolesAllowed({ADMIN, STUDENT})
    public void resign(Member member) {
        if (member instanceof Student student) {
            s3Service.remove(student.getProfileImage().getUrl());
        }
        memberServiceFacade.resign(member);
    }

    @PostMapping("{id}/friendship-requests")
    @ResponseStatus(HttpStatus.CREATED)
    @RolesAllowed({STUDENT})
    @Operation(description = "친구신청을 합니다.")
    public void requestFriendship(Student user, @PathVariable UUID id,
        @RequestBody SendFriendShipRequest dto) {
        try {
            Student target = (Student) memberServiceFacade.find(id);
            memberServiceFacade.requestFriendship(user, target, dto.message());
        } catch (ClassCastException e) {
            throw new MemberNotFoundException();
        }
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
    public void patch(@RequestBody @Valid Patch dto, Member target) {
        if (target instanceof Admin) {
            Admin source = mapper.toEntity(dto);
            memberServiceFacade.patch(source, target);
            return;
        }
        if (target instanceof Student) {
            Image profile = s3Service.upload(dto.profileImage());
            Image certificateImage = s3Service.upload(dto.studentCertificationImage());
            Student source = mapper.toEntity(dto, profile, certificateImage);
            memberServiceFacade.patch(source, target);
        }
    }

    @GetMapping("/me")
    @RolesAllowed({STUDENT})
    public Detail getMe(Student member) {
        return mapper.toDetail(member);
    }
}

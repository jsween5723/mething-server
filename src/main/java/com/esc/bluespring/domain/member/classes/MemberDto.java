package com.esc.bluespring.domain.member.classes;

import com.esc.bluespring.common.enums.Gender;
import com.esc.bluespring.common.enums.MBTI;
import com.esc.bluespring.domain.university.major.classes.MajorDto.SearchListElement;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

public record MemberDto() {

    public record Join(@RequestPart("profileImage") MultipartFile profileImage,
                       @NotBlank @Length(min = 2, max = 10, message = "닉네임은 2~10 글자입니다.") @NotNull String nickname,
                       @NotNull @NotBlank String name,
                       @NotNull @NotBlank String password,
                       @NotNull @DateTimeFormat(iso = ISO.DATE) LocalDate birthday,
                       @NotNull Long majorId, @NotNull @Email String email,
                       @RequestPart("studentCertificationImageUrl") MultipartFile studentCertificationImage,
                       @NotNull Gender gender, MBTI mbti) {

    }

    public record Patch(@RequestPart("profileImage") MultipartFile profileImage,
                        @NotBlank @Length(min = 2, max = 10, message = "닉네임은 2~10 글자입니다.") String nickname,
                        @NotBlank String password,
                        @DateTimeFormat(iso = ISO.DATE) LocalDate birthday,
                        Long majorId, @Email String email,
                        @RequestPart("studentCertificationImageUrl") MultipartFile studentCertificationImage,
                        Gender gender, MBTI mbti) {

    }

    public record AdminJoin(@NotNull @NotBlank String password, @NotNull @Email String email) {

    }

    public record Login(@NotNull @NotBlank String password, @NotNull @Email String email) {

    }

    public record JwtToken(String accessToken) {}


    public record Detail(Long id, String profileImageUrl, String nickname, SearchListElement major,
                         Gender gender) {

    }

    public record SendFriendShipRequest(String message) {

    }
}

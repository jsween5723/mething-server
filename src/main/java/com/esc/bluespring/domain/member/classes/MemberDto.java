package com.esc.bluespring.domain.member.classes;

import com.esc.bluespring.common.enums.Gender;
import com.esc.bluespring.common.enums.MBTI;
import com.esc.bluespring.domain.member.entity.profile.BioPattern;
import com.esc.bluespring.domain.member.entity.profile.Drink;
import com.esc.bluespring.domain.member.entity.profile.Smoke;
import com.esc.bluespring.domain.member.entity.profile.Sport;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Set;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

public record MemberDto() {

    public record JoinRequest(String profileImage,
                              @NotBlank @Length(min = 2, max = 10, message = "닉네임은 2~10 글자입니다.") @NotNull String nickname,
                              @NotNull String introduce, @NotNull @NotBlank String password,
                              @NotNull @DateTimeFormat(iso = ISO.DATE) LocalDate birthday,
                              @NotNull @Email String email, @NotNull Gender gender,
                              ProfileDto profile,
                              @NotNull SchoolInformationDto schoolInformation) {

    }

    public record ProfileDto(MBTI mbti, @Schema(description = "키") Double stature, Smoke smoke,
                             Drink drink, Sport sport, BioPattern bioPattern,
                             Set<String> interests) {

    }

    public record SchoolInformationDto(@NotNull @NotBlank String name,
                                       String studentCertificationImage,
                                       @NotNull Long major) {

    }

    public record AdminJoin(@NotNull @NotBlank String password, @NotNull @Email String email) {

    }

    public record Login(@NotNull @NotBlank String password, @NotNull @Email String email) {

    }

    public record JwtToken(String accessToken) {

    }

    public record SendFriendShipRequest(String message) {

    }
}

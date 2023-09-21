package com.esc.bluespring.domain.auth.classes;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

public record AuthDto() {

  public record PhoneCodeGenerate(@NotNull @NotBlank(message = "핸드폰 번호를 입력해주세요.") String phoneNumber) {

    @Builder
    public PhoneCodeGenerate {
    }
  }

  public record PhoneCodeAuthenticate(@NotNull @NotBlank(message = "인증 코드를 입력해주세요.") String phoneCode) {

    @Builder
    public PhoneCodeAuthenticate {
    }
  }

  public record EmailCodeGenerate(@NotNull @Email String email) {}
  public record EmailCodeAuthenticate(@NotNull @NotBlank String code) {}
}

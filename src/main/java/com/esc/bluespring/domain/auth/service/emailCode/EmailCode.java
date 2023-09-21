package com.esc.bluespring.domain.auth.service.emailCode;

import com.esc.bluespring.domain.auth.exception.AuthException.PhoneCodeNotValidException;
import java.util.Random;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;


@Getter
class EmailCode {

  private final String code;
  private boolean authenticated;

  @Builder(access = AccessLevel.PACKAGE)
  private EmailCode(String code, boolean authenticated) {
    this.code = code;
    this.authenticated = authenticated;
  }

  public EmailCode() {
    this.code = makeRandomKey();
    this.authenticated = false;
  }

  public void authenticate(String code) {
    if (!this.code.equals(code)) {
      throw new PhoneCodeNotValidException();
    }
    authenticated = true;
  }

  private String makeRandomKey() {
    Random random = new Random();
    int randomNumber = 1000 + random.nextInt(9000);
    return Integer.toString(randomNumber);
  }
}


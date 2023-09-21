package com.esc.bluespring.domain.auth.service.phoneCode;

import com.esc.bluespring.domain.auth.exception.AuthException.PhoneCodeNotValidException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PhoneCodeTest {

  @Test
  @DisplayName("올바르지 않은 코드일 경우 에러를 반환한다.")
  void isUnvalidCode() {
    PhoneCode phoneCode = new PhoneCode();
    Assertions.assertThrows(PhoneCodeNotValidException.class,
        () -> phoneCode.authenticate(phoneCode.getCode() + "2"));
    Assertions.assertFalse(phoneCode.isAuthenticated());
  }

  @Test
  @DisplayName("올바른 코드일 경우 상태를 변경한다.")
  void getCode() {
    PhoneCode phoneCode = new PhoneCode();
    Assertions.assertDoesNotThrow(() -> phoneCode.authenticate(phoneCode.getCode()));
    Assertions.assertTrue(phoneCode.isAuthenticated());
  }
}
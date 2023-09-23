package com.esc.bluespring.domain.auth.service.phoneCode;

import static org.mockito.Mockito.when;

import com.esc.bluespring.common.cache.ExpireConcurrentMapCache;
import com.esc.bluespring.common.configuration.CacheConfiguration;
import com.esc.bluespring.domain.auth.exception.AuthException.PhoneCodeNotFoundException;
import com.esc.bluespring.domain.auth.exception.AuthException.PhoneCodeNotValidException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cache.CacheManager;

@ExtendWith(MockitoExtension.class)
class PhoneAuthenticatorTest {

  @InjectMocks
  private PhoneAuthenticator authenticator;
  @Mock
  private CacheManager cacheManager;

  @BeforeEach
  public void each() {
    ExpireConcurrentMapCache cache = new ExpireConcurrentMapCache("test", 3L);
    when(cacheManager.getCache(CacheConfiguration.CODE_CACHE_NAME)).thenReturn(cache, cache);
  }

  @Test
  @DisplayName("이미 존재하는 경우에 대해 isExists true 반환")
  public void isExistTest() {
    //given
    PhoneCode code = PhoneCode.builder()
        .code("1234")
        .authenticated(false)
        .build();
    authenticator.assignPhoneCode("01011111111", code);
    //둘다 존재
    Assertions.assertTrue(authenticator.isExistCode("01011111111", "1234"));
    //인증중인 휴대폰 번호 없음
    Assertions.assertFalse(authenticator.isExistCode("01011111112", "1234"));
    //휴대폰 O 코드 X
    Assertions.assertFalse(authenticator.isExistCode("01011111111", "1233"));
    //휴대폰 X 코드 X
    Assertions.assertFalse(authenticator.isExistCode("01011111112", "1233"));
  }

  @Test
  @DisplayName("인증된 휴대폰일 경우 true 아닐 경우 false, 기록이 없다면 예외반환 isAuthenticatedPhone")
  public void isauthenticatedPhoneTest() {
    //given
    PhoneCode code = PhoneCode.builder()
        .code("1234")
        .authenticated(false)
        .build();
    //없을 때
    Assertions.assertThrows(PhoneCodeNotFoundException.class,
        () -> authenticator.isAuthenticatedPhone("01011111111"));
    //있으나 인증되지 않았을 때
    authenticator.assignPhoneCode("01011111111", code);
    Assertions.assertFalse(authenticator.isAuthenticatedPhone("01011111111"));

    //있으나 인증 실패했을 때
    Assertions.assertThrows(PhoneCodeNotValidException.class,
        () -> authenticator.authenticatePhoneCode("01011111111", "1111"));
    Assertions.assertFalse(authenticator.isAuthenticatedPhone("01011111111"));

    //있고 인증 성공된 후
    authenticator.authenticatePhoneCode("01011111111", "1234");
    Assertions.assertTrue(authenticator.isAuthenticatedPhone("01011111111"));
  }
}
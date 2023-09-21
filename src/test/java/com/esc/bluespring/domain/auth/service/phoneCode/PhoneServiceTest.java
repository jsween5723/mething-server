package com.esc.bluespring.domain.auth.service.phoneCode;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.esc.bluespring.common.utils.sms.service.SmsService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PhoneServiceTest {

  @InjectMocks
  private PhoneService phoneService;
  @Mock
  private PhoneAuthenticator phoneAuthenticator;
  @Mock
  private SmsService smsService;

  @Test
  @DisplayName("인증 번호를 생성할 때, 기존 코드와 중복된 코드일 경우 재생성한다.")
  void regenereatePhoneCode() {
    when(phoneAuthenticator.isExistCode(any(),any())).thenReturn(true,false);
    phoneService.genereatePhoneCode("01011111111");
    verify(phoneAuthenticator, times(2)).isExistCode(eq("01011111111"),any());
  }

  @Test
  @DisplayName("정상적으로 생성된 후 문자를 발송한다.")
  void authenticatePhoneCode() {
    when(phoneAuthenticator.isExistCode(any(),any())).thenReturn(false);
    phoneService.genereatePhoneCode("01011111111");
    verify(smsService, times(1)).sendSMS(any());
  }
}
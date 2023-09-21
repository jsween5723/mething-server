package com.esc.bluespring.domain.auth.service.phoneCode;

import com.esc.bluespring.common.utils.sms.dto.MessageDto;
import com.esc.bluespring.common.utils.sms.service.SmsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PhoneService {
  private final SmsService smsService;
  private final PhoneAuthenticator authenticator;
  public void genereatePhoneCode(String phoneNumber) {
    PhoneCode code = new PhoneCode();
    while (authenticator.isExistCode(phoneNumber, code.getCode())) {
      code = new PhoneCode();
    }
    authenticator.assignPhoneCode(phoneNumber, code);
    MessageDto dto = new MessageDto(phoneNumber, code.getCode());
    smsService.sendSMS(dto);
  }

  public void authenticatePhoneCode(String phoneNumber, String code) {
    authenticator.authenticatePhoneCode(phoneNumber, code);
  }

  public void isAuthenticatedPhone(String phoneNumber) {
    authenticator.isAuthenticatedPhone(phoneNumber);
  }
}

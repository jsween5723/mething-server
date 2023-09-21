package com.esc.bluespring.domain.auth.service.emailCode;

import com.esc.bluespring.domain.auth.service.emailCode.EmailCodeMessageFactory.EmailCodeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailAuthenticationService {
  private final JavaMailSender sender; //빨간 줄 무시해도 됩니다.
  private final EmailAuthenticator authenticator;
  private final EmailCodeMessageFactory messageFactory;
  public void genereate(String email) {
    EmailCode code = new EmailCode();
    while (authenticator.isExistCode(email, code.getCode())) {
      code = new EmailCode();
    }
    authenticator.assignPhoneCode(email, code);
    EmailCodeMessage dto = messageFactory.generate(email, code.getCode());
    sender.send(dto);
  }

  public void authenticate(String email, String code) {
    authenticator.authenticatePhoneCode(email, code);
  }

  public void isAuthenticated(String email) {
    authenticator.isAuthenticatedPhone(email);
  }
}

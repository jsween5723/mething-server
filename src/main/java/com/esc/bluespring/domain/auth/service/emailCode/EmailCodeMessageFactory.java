package com.esc.bluespring.domain.auth.service.emailCode;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Component;

@Component
public class EmailCodeMessageFactory {
    @Value("${spring.mail.from}")
    private String from;

    public EmailCodeMessage generate(String email, String code) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(email);
        mailMessage.setText(code);
        return new EmailCodeMessage(mailMessage);
    }
    public class EmailCodeMessage extends SimpleMailMessage {
        static private final String SUBJECT = "미띵 가입인증번호 메일이에요!";

        EmailCodeMessage(SimpleMailMessage original) {
            super(original);
            setSubject(SUBJECT);
            setFrom(from);
            setText(generateMessage(getText()));
        }

        private String generateMessage(String code) {
            return code;
        }
    }
}


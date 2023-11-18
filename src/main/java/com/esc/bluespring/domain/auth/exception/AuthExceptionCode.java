package com.esc.bluespring.domain.auth.exception;

import com.esc.bluespring.common.exception.ExceptionCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
enum AuthExceptionCode implements ExceptionCode {
  UNVALID_PHONE_CODE(HttpStatus.UNAUTHORIZED, "휴대폰 인증번호가 일치하지 않습니다."),
  EXPIRED_PHONE_CODE(HttpStatus.UNAUTHORIZED, "휴대폰 인증번호가 만료되었습니다."),
  PHONE_CODE_NOT_FOUND(HttpStatus.UNAUTHORIZED, "인증정보와 일치하는 휴대폰 인증번호가 존재하지 않습니다."),
  LOGIN_FAILED(HttpStatus.UNAUTHORIZED, "비밀번호 혹은 계정이 일치하지 않습니다."),
  LOGIN_REQUIRED(HttpStatus.UNAUTHORIZED, "로그인이 필요합니다."),
  FORBIDDEN(HttpStatus.FORBIDDEN, "권한이 없습니다."),
  EMAIL_CODE_NOT_FOUND(HttpStatus.UNAUTHORIZED, "인증정보와 일치하는 이메일 인증번호가 존재하지 않습니다."),
  SESSION_HAS_NOT_EMAIL(HttpStatus.NOT_FOUND, "현재 세션에 이메일 정보가 없습니다.");
  private final HttpStatus httpStatus;
  private final String message;

  AuthExceptionCode(HttpStatus httpStatus, String message) {
    this.httpStatus = httpStatus;
    this.message = message;
  }

  @Override
  public String getCode() {
    return this.name();
  }

}

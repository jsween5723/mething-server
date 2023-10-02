package com.esc.bluespring.domain.member.exception;

import com.esc.bluespring.common.exception.ExceptionCode;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor(access = AccessLevel.PACKAGE)
public enum MemberExceptionCode implements ExceptionCode {

  DUPLICATED_NICKNAME(HttpStatus.CONFLICT, "중복된 닉네임입니다."),
  DUPLICATED_EMAIL(HttpStatus.CONFLICT, "중복된 로그인 이메일입니다."),
  DUPLICATED_SCHOOL_EMAIL(HttpStatus.CONFLICT, "중복된 학교 이메일입니다."),
  MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 사용자입니다."),
  STUDENT_NOT_CERTIFICATED(HttpStatus.NOT_FOUND, "학교인증이 안된 사용자입니다.");
  private final HttpStatus httpStatus;
  private String message;

  public MemberExceptionCode appended(Object o) {
    message = message + " {" + o.toString() + "}";
    return this;
  }

  @Override
  public String getCode() {
    return this.name();
  }

}

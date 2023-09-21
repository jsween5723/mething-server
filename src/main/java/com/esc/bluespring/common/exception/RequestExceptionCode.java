package com.esc.bluespring.common.exception;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor(access = AccessLevel.PACKAGE)
enum RequestExceptionCode implements ExceptionCode {

  REQUEST_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 요청입니다."),
  REQUEST_IS_NOT_OWN(HttpStatus.FORBIDDEN, "요청에 대한 권한이 없습니다.");
  private final HttpStatus httpStatus;
  private String message;

  public RequestExceptionCode appended(Object o) {
    message = message + " {" + o.toString() + "}";
    return this;
  }

  @Override
  public String getCode() {
    return this.name();
  }
}

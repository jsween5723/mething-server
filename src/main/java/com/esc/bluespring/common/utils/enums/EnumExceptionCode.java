package com.esc.bluespring.common.utils.enums;

import com.esc.bluespring.common.exception.ExceptionCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum EnumExceptionCode implements ExceptionCode {

  INVALID_ENUM(HttpStatus.BAD_REQUEST, "잘못된 enum 값입니다."),
  ;

  private final HttpStatus httpStatus;
  private String message;

  EnumExceptionCode(HttpStatus httpStatus, String message) {
    this.httpStatus = httpStatus;
    this.message = message;
  }

  public EnumExceptionCode appended(Object o) {
    message = message + "{" + o.toString() + "}";
    return this;
  }

  @Override
  public String getCode() {
    return this.name();
  }
}

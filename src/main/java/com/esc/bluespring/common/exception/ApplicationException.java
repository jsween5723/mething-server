package com.esc.bluespring.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public abstract class ApplicationException extends RuntimeException {

  private final String code;
  private final HttpStatus httpStatus;

  protected ApplicationException(ExceptionCode exceptionCode) {
    super(exceptionCode.getMessage());
    this.code = exceptionCode.getCode();
    this.httpStatus = exceptionCode.getHttpStatus();
  }

}

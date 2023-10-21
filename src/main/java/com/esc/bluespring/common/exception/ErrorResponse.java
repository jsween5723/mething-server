package com.esc.bluespring.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ErrorResponse {

  private final String code;
  private final String message;
  private final HttpStatus status;

  private ErrorResponse(String code, String message, HttpStatus status) {
    this.code = code;
    this.message = message;
    this.status = status;
  }

  public static ErrorResponse of(ApplicationException exception) {
    return new ErrorResponse(exception.getCode(), exception.getMessage(), exception.getHttpStatus());
  }

  public static ErrorResponse of(String code, String message, HttpStatus status) {
    return new ErrorResponse(code, message, status);
  }

}

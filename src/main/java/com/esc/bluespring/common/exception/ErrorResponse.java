package com.esc.bluespring.common.exception;

import lombok.Getter;

@Getter
class ErrorResponse {

  private final String code;
  private final String message;

  private ErrorResponse(String code, String message) {
    this.code = code;
    this.message = message;
  }

  public static ErrorResponse of(ApplicationException exception) {
    return new ErrorResponse(exception.getCode(), exception.getMessage());
  }

  public static ErrorResponse of(String code, String message) {
    return new ErrorResponse(code, message);
  }

}

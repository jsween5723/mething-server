package com.esc.bluespring.domain.university.major.exception;

import com.esc.bluespring.common.exception.ExceptionCode;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor(access = AccessLevel.PACKAGE)
public enum MajorExceptionCode implements ExceptionCode {

  MAJOR_NOT_FOUND_EXCEPTION(HttpStatus.NOT_FOUND, "존재하지 않는 학과입니다.");

  private final HttpStatus httpStatus;
  private final String message;

  @Override
  public String getCode() {
    return this.name();
  }

}

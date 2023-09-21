package com.esc.bluespring.domain.university.exception;

import com.esc.bluespring.common.exception.ExceptionCode;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor(access = AccessLevel.PACKAGE)
enum UniversityExceptionCode implements ExceptionCode {

  UNIVERSITY_NOT_FOUND_EXCEPTION(HttpStatus.NOT_FOUND, "존재하지 않는 대학 정보입니다."),
  ;

  private final HttpStatus httpStatus;
  private final String message;

  @Override
  public String getCode() {
    return this.name();
  }
}

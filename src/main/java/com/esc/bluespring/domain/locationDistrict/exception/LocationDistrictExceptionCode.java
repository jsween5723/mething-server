package com.esc.bluespring.domain.locationDistrict.exception;

import com.esc.bluespring.common.exception.ExceptionCode;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor(access = AccessLevel.PACKAGE)
enum LocationDistrictExceptionCode implements ExceptionCode {
  LOCATION_DISTRICT_NOT_FOUND_EXCEPTION(HttpStatus.NOT_FOUND, "존재하지 않는 지역 정보입니다.");

  private final HttpStatus httpStatus;
  private String message;

  public LocationDistrictExceptionCode appended(Object o) {
    message = message + " {" + o.toString() + "}";
    return this;
  }

  @Override
  public String getCode() {
    return this.name();
  }

}

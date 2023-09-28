package com.esc.bluespring.domain.locationDistrict.exception;

import static com.esc.bluespring.domain.locationDistrict.exception.LocationDistrictExceptionCode.LOCATION_DISTRICT_NOT_FOUND_EXCEPTION;

import com.esc.bluespring.common.exception.ApplicationException;
import com.esc.bluespring.common.exception.ExceptionCode;
import java.util.List;

public abstract class LocationDistrictException extends ApplicationException {

  protected LocationDistrictException(ExceptionCode exceptionCode) {
    super(exceptionCode, null);
  }
  public static class LocationDistrictNotFoundException extends LocationDistrictException {

    public LocationDistrictNotFoundException() {
      super(LOCATION_DISTRICT_NOT_FOUND_EXCEPTION);
    }

    public LocationDistrictNotFoundException(List<String> nameList) {
      super(LOCATION_DISTRICT_NOT_FOUND_EXCEPTION.appended(nameList));
    }
  }
}

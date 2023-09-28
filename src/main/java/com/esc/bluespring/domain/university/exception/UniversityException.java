package com.esc.bluespring.domain.university.exception;

import com.esc.bluespring.common.exception.ApplicationException;

public abstract class UniversityException extends ApplicationException {
  protected UniversityException(UniversityExceptionCode exceptionCode) {
    super(exceptionCode, null);
  }


  public static class UniversityNotFoundException extends UniversityException {

    public UniversityNotFoundException() {
      super(UniversityExceptionCode.UNIVERSITY_NOT_FOUND_EXCEPTION);
    }

  }
}

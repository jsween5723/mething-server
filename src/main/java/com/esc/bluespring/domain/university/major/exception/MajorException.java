package com.esc.bluespring.domain.university.major.exception;

import com.esc.bluespring.common.exception.ApplicationException;

public abstract class MajorException extends ApplicationException {

  protected MajorException(MajorExceptionCode exceptionCode) {
    super(exceptionCode);
  }

  public static class MajorNotFoundException extends MajorException {
    public MajorNotFoundException() {
      super(MajorExceptionCode.MAJOR_NOT_FOUND_EXCEPTION);
    }
  }
}

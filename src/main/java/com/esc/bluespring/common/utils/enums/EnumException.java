package com.esc.bluespring.common.utils.enums;

import com.esc.bluespring.common.utils.exception.UtilException;

public abstract class EnumException extends UtilException {

  protected EnumException(EnumExceptionCode exceptionCode) {
    super(exceptionCode);
  }

  public static class InvalidEnumException extends EnumException {

    public InvalidEnumException() {
      super(EnumExceptionCode.INVALID_ENUM);
    }

  }

}

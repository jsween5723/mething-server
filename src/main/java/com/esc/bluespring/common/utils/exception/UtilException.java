package com.esc.bluespring.common.utils.exception;

import com.esc.bluespring.common.exception.ApplicationException;
import com.esc.bluespring.common.exception.ExceptionCode;

public abstract class UtilException extends ApplicationException {

  protected UtilException(ExceptionCode exceptionCode) {
    super(exceptionCode);
  }

}

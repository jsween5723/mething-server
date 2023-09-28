package com.esc.bluespring.common.exception;

import static com.esc.bluespring.common.exception.RequestExceptionCode.REQUEST_IS_NOT_OWN;
import static com.esc.bluespring.common.exception.RequestExceptionCode.REQUEST_NOT_FOUND;

public abstract class RequestException extends ApplicationException {

  RequestException(RequestExceptionCode exceptionCode) {
    super(exceptionCode, null);
  }

  public static class RequestNotFoundException extends RequestException {

    public RequestNotFoundException() {
      super(REQUEST_NOT_FOUND);
    }
  }

  public static class NotOwnerException extends RequestException {

    public NotOwnerException() {
      super(REQUEST_IS_NOT_OWN);
    }
  }
}

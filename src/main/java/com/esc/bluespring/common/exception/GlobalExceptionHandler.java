package com.esc.bluespring.common.exception;

import com.esc.bluespring.domain.auth.exception.AuthException.ForbiddenException;
import com.esc.bluespring.domain.auth.exception.AuthException.LoginRequiredException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

  private static final String EXCEPTION_LOG_TEMPLATE = "code = {}, message = {}";

  @ExceptionHandler(AccessDeniedException.class)
  @ResponseStatus(HttpStatus.FORBIDDEN)
  public ErrorResponse accessDeniedException(AccessDeniedException e) {
    ForbiddenException exception = new ForbiddenException(e);
    log.error(exception.getCause().getMessage());
    return ErrorResponse.of(exception);
  }

  @ExceptionHandler(AuthenticationException.class)
  @ResponseStatus(HttpStatus.UNAUTHORIZED)
  public ErrorResponse authenticationException(AuthenticationException e) {
    LoginRequiredException loginRequiredException = new LoginRequiredException(e);
    log.error(loginRequiredException.getCause().getMessage());
    return ErrorResponse.of(loginRequiredException);
  }

  @ExceptionHandler(ApplicationException.class)
  public ResponseEntity<ErrorResponse> applicationException(ApplicationException e) {

    String code = e.getCode();
    String message = e.getMessage();
    HttpStatus status = e.getHttpStatus();

    log.warn(EXCEPTION_LOG_TEMPLATE, code, message);
    ErrorResponse errorResponse = ErrorResponse.of(e);

    return ResponseEntity
        .status(status)
        .body(errorResponse);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> methodArgumentNotValidException(MethodArgumentNotValidException e) {

    String code = e.getStatusCode().toString();
    HttpStatus status = (HttpStatus) e.getStatusCode();
    BindingResult bindingResult = e.getBindingResult();
    FieldError fieldError = bindingResult.getFieldErrors().get(0);
    String message = fieldError.getDefaultMessage();

    log.warn(EXCEPTION_LOG_TEMPLATE, code, message);
    ErrorResponse errorResponse = ErrorResponse.of(code, message);

    return ResponseEntity
        .status(status)
        .body(errorResponse);
  }

}

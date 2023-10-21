package com.esc.bluespring.common.exception;

import com.esc.bluespring.common.BaseResponse;
import com.esc.bluespring.domain.auth.exception.AuthException.ForbiddenException;
import com.esc.bluespring.domain.auth.exception.AuthException.LoginRequiredException;
import discord4j.discordjson.json.EmbedData;
import discord4j.discordjson.json.EmbedFieldData;
import discord4j.rest.entity.RestChannel;
import jakarta.servlet.http.HttpServletRequest;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class GlobalExceptionHandler {

  private static final String EXCEPTION_LOG_TEMPLATE = "code = {}, message = {}";
  private final RestChannel channel;

  @ExceptionHandler(AccessDeniedException.class)
  @ResponseStatus(HttpStatus.FORBIDDEN)
  public BaseResponse<ErrorResponse> accessDeniedException(AccessDeniedException e) {
    ForbiddenException exception = new ForbiddenException(e);
    log.error(exception.getCause().getMessage());
    return new BaseResponse<>(ErrorResponse.of(exception));
  }

  @ExceptionHandler(AuthenticationException.class)
  @ResponseStatus(HttpStatus.UNAUTHORIZED)
  public BaseResponse<ErrorResponse> authenticationException(AuthenticationException e) {
    LoginRequiredException loginRequiredException = new LoginRequiredException(e);
    log.error(loginRequiredException.getCause().getMessage());
    return new BaseResponse<>(ErrorResponse.of(loginRequiredException));
  }

  @ExceptionHandler(ApplicationException.class)
  public ResponseEntity<BaseResponse<ErrorResponse>> applicationException(ApplicationException e) {

    String code = e.getCode();
    String message = e.getMessage();
    HttpStatus status = e.getHttpStatus();

    log.warn(EXCEPTION_LOG_TEMPLATE, code, message);
    ErrorResponse errorResponse = ErrorResponse.of(e);

    return ResponseEntity.status(status).body(new BaseResponse<>(errorResponse));
  }


  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<BaseResponse<ErrorResponse>> methodArgumentNotValidException(
      MethodArgumentNotValidException e) {

    String code = e.getStatusCode().toString();
    HttpStatus status = (HttpStatus) e.getStatusCode();
    BindingResult bindingResult = e.getBindingResult();
    FieldError fieldError = bindingResult.getFieldErrors().get(0);
    String message = fieldError.getDefaultMessage();

    log.warn(EXCEPTION_LOG_TEMPLATE, code, message);
    ErrorResponse errorResponse = ErrorResponse.of(code, message, HttpStatus.BAD_REQUEST);

    return ResponseEntity.status(status).body(new BaseResponse<>(errorResponse));
  }
  @ExceptionHandler(Exception.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public BaseResponse<ErrorResponse> internalException(Exception exception,
                                         HttpServletRequest request) {

    StringWriter sw = new StringWriter();
    exception.printStackTrace(new PrintWriter(sw));
    String exceptionAsString = sw.toString();
    channel.createMessage(
        EmbedData.builder().title(exception.getLocalizedMessage().length() > 250 ? exception.getLocalizedMessage().substring(0,250) : exception.getLocalizedMessage()).description(exceptionAsString.substring(0,2000))
            .addField(EmbedFieldData.builder().name("uri").value(request.getRequestURI()).build())
            .addField(EmbedFieldData.builder().name("authorization")
                .value(request.getHeader("Authorization") == null ? "" :request.getHeader("Authorization")).build()).addField(
                EmbedFieldData.builder().name("parameters").value(request.getQueryString())
                    .build()).timestamp(LocalDateTime.now(ZoneId.of("Asia/Seoul")).format(
                DateTimeFormatter.ISO_LOCAL_DATE_TIME)).build())
            .block();
    return new BaseResponse<>(ErrorResponse.of(exception.getMessage(), exception.getLocalizedMessage(), HttpStatus.INTERNAL_SERVER_ERROR));
  }

}

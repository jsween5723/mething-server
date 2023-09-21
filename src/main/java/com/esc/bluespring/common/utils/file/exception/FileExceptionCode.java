package com.esc.bluespring.common.utils.file.exception;

import com.esc.bluespring.common.exception.ExceptionCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
enum FileExceptionCode implements ExceptionCode {

  FILE_FORMAT_NOT_ALLOW(HttpStatus.BAD_REQUEST, "허용되지 않은 파일 확장자입니다."),
  FAIL_TO_PARSE_FILE(HttpStatus.INTERNAL_SERVER_ERROR, "파일 파싱에 실패했습니다."),
  CONTENT_TYPE_NOT_ALLOW(HttpStatus.BAD_REQUEST, "잘못된 타입의 파일입니다."),
  FAIL_TO_UPLOAD_S3(HttpStatus.INTERNAL_SERVER_ERROR, "S3 업로드에 실패했습니다."),
  FAIL_TO_DELETE_S3(HttpStatus.INTERNAL_SERVER_ERROR, "S3 삭제에 실패했습니다."),
  FAIL_TO_FETCH_S3(HttpStatus.INTERNAL_SERVER_ERROR, "S3 조회에 실패했습니다."),
  NO_ATTACHMENT(HttpStatus.BAD_REQUEST, "첨부 파일이 존재하지 않습니다."),
  ;

  private final HttpStatus httpStatus;
  private String message;

  FileExceptionCode(HttpStatus httpStatus, String message) {
    this.httpStatus = httpStatus;
    this.message = message;
  }

  public FileExceptionCode appended(Object o) {
    message = message + "{" + o.toString() + "}";
    return this;
  }

  @Override
  public String getCode() {
    return this.name();
  }

}

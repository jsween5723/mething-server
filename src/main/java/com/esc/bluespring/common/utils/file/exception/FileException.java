package com.esc.bluespring.common.utils.file.exception;

import static com.esc.bluespring.common.utils.file.exception.FileExceptionCode.CONTENT_TYPE_NOT_ALLOW;
import static com.esc.bluespring.common.utils.file.exception.FileExceptionCode.FAIL_TO_DELETE_S3;
import static com.esc.bluespring.common.utils.file.exception.FileExceptionCode.FAIL_TO_FETCH_S3;
import static com.esc.bluespring.common.utils.file.exception.FileExceptionCode.FAIL_TO_PARSE_FILE;
import static com.esc.bluespring.common.utils.file.exception.FileExceptionCode.FAIL_TO_UPLOAD_S3;
import static com.esc.bluespring.common.utils.file.exception.FileExceptionCode.FILE_FORMAT_NOT_ALLOW;
import static com.esc.bluespring.common.utils.file.exception.FileExceptionCode.NO_ATTACHMENT;

import com.esc.bluespring.common.utils.exception.UtilException;

public abstract class FileException extends UtilException {

  protected FileException(FileExceptionCode exceptionCode) {
    super(exceptionCode);
  }

  public static class FileFormatException extends FileException {

    public FileFormatException() {
      super(FILE_FORMAT_NOT_ALLOW);
    }

    public FileFormatException(String format) {
      super(FILE_FORMAT_NOT_ALLOW.appended(format));
    }
  }

  public static class FileParseException extends FileException {

    public FileParseException() {
      super(FAIL_TO_PARSE_FILE);
    }
  }

  public static class FileContentTypeException extends FileException {

    public FileContentTypeException() {
      super(CONTENT_TYPE_NOT_ALLOW);
    }

    public FileContentTypeException(String contentType) {
      super(CONTENT_TYPE_NOT_ALLOW.appended(contentType));
    }
  }

  public static class S3UploadFailException extends FileException {

    public S3UploadFailException() {
      super(FAIL_TO_UPLOAD_S3);
    }
  }

  public static class S3DeleteFailException extends FileException {

    public S3DeleteFailException() {
      super(FAIL_TO_DELETE_S3);
    }
  }

  public static class S3FetchFailException extends FileException {

    public S3FetchFailException() {
      super(FAIL_TO_FETCH_S3);
    }
  }

  public static class NoAttachmentException extends FileException {

    public NoAttachmentException() {
      super(NO_ATTACHMENT);
    }
  }

}

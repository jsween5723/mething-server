package com.esc.bluespring.domain.member.exception;

import static com.esc.bluespring.domain.member.exception.MemberExceptionCode.DUPLICATED_EMAIL;
import static com.esc.bluespring.domain.member.exception.MemberExceptionCode.DUPLICATED_NICKNAME;
import static com.esc.bluespring.domain.member.exception.MemberExceptionCode.DUPLICATED_SCHOOL_EMAIL;
import static com.esc.bluespring.domain.member.exception.MemberExceptionCode.LOGIN_REQUIRED;
import static com.esc.bluespring.domain.member.exception.MemberExceptionCode.MEMBER_NOT_FOUND;
import static com.esc.bluespring.domain.member.exception.MemberExceptionCode.STUDENT_NOT_CERTIFICATED;

import com.esc.bluespring.common.exception.ApplicationException;

public abstract class MemberException extends ApplicationException {

  protected MemberException(MemberExceptionCode exceptionCode) {
    super(exceptionCode);
  }

  public static class MemberNotFoundException extends MemberException {
    public MemberNotFoundException() {
      super(MemberExceptionCode.MEMBER_NOT_FOUND);
    }

    public MemberNotFoundException(Object o) {
      super(MEMBER_NOT_FOUND.appended(o));
    }
  }

  static public class DuplicateNicknameException extends MemberException {

    public DuplicateNicknameException() {
      super(DUPLICATED_NICKNAME);
    }
  }

  static public class DuplicateEmailException extends MemberException {

    public DuplicateEmailException() {
      super(DUPLICATED_EMAIL);
    }
  }

  static public class DuplicateSchoolEmailException extends MemberException {

    public DuplicateSchoolEmailException() {
      super(DUPLICATED_SCHOOL_EMAIL);
    }
  }

  static public class LoginRequiredException extends MemberException {

    public LoginRequiredException(Object o) {
      super(LOGIN_REQUIRED.appended(o));
    }
    public LoginRequiredException() {
      super(LOGIN_REQUIRED);
    }
  }

  static public class StudentNotCertificatedException extends MemberException{

    public StudentNotCertificatedException() {
      super(STUDENT_NOT_CERTIFICATED);
    }
  }
}

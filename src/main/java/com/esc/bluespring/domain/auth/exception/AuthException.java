package com.esc.bluespring.domain.auth.exception;

import static com.esc.bluespring.domain.auth.exception.AuthExceptionCode.EMAIL_CODE_NOT_FOUND;
import static com.esc.bluespring.domain.auth.exception.AuthExceptionCode.FORBIDDEN;
import static com.esc.bluespring.domain.auth.exception.AuthExceptionCode.LOGIN_FAILED;
import static com.esc.bluespring.domain.auth.exception.AuthExceptionCode.LOGIN_REQUIRED;
import static com.esc.bluespring.domain.auth.exception.AuthExceptionCode.PHONE_CODE_NOT_FOUND;
import static com.esc.bluespring.domain.auth.exception.AuthExceptionCode.UNVALID_PHONE_CODE;

import com.esc.bluespring.common.exception.ApplicationException;
import com.esc.bluespring.common.exception.ExceptionCode;

public abstract class AuthException extends ApplicationException {

    protected AuthException(ExceptionCode exceptionCode) {
        super(exceptionCode);
    }

    public static class PhoneCodeNotFoundException extends AuthException {

        public PhoneCodeNotFoundException() {
            super(PHONE_CODE_NOT_FOUND);
        }
    }

    public static class PhoneCodeNotValidException extends AuthException {

        public PhoneCodeNotValidException() {
            super(UNVALID_PHONE_CODE);
        }
    }

    public static class EmailCodeNotFoundException extends AuthException {

        public EmailCodeNotFoundException() {
            super(EMAIL_CODE_NOT_FOUND);
        }
    }

    public static class LoginFailedException extends AuthException {

        public LoginFailedException() {
            super(LOGIN_FAILED);
        }
    }

    public static class LoginRequiredException extends AuthException {

        public LoginRequiredException() {
            super(LOGIN_REQUIRED);
        }
    }

    public static class ForbiddenException extends AuthException {

        public ForbiddenException() {
            super(FORBIDDEN);
        }
    }
}

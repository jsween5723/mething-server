package com.esc.bluespring.domain.auth.exception;

import static com.esc.bluespring.domain.auth.exception.AuthExceptionCode.EMAIL_CODE_NOT_FOUND;
import static com.esc.bluespring.domain.auth.exception.AuthExceptionCode.FORBIDDEN;
import static com.esc.bluespring.domain.auth.exception.AuthExceptionCode.GO_TO_LOGIN;
import static com.esc.bluespring.domain.auth.exception.AuthExceptionCode.LOGIN_FAILED;
import static com.esc.bluespring.domain.auth.exception.AuthExceptionCode.LOGIN_REQUIRED;
import static com.esc.bluespring.domain.auth.exception.AuthExceptionCode.PHONE_CODE_NOT_FOUND;
import static com.esc.bluespring.domain.auth.exception.AuthExceptionCode.SESSION_HAS_NOT_EMAIL;
import static com.esc.bluespring.domain.auth.exception.AuthExceptionCode.UNVALID_PHONE_CODE;

import com.esc.bluespring.common.exception.ApplicationException;
import com.esc.bluespring.common.exception.ExceptionCode;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;

public abstract class AuthException extends ApplicationException {

    protected AuthException(ExceptionCode exceptionCode) {
        super(exceptionCode, null);
    }
    protected AuthException(ExceptionCode exceptionCode, Throwable e) {
        super(exceptionCode, e);
    }

    public static class PhoneCodeNotFoundException extends AuthException {

        public PhoneCodeNotFoundException() {
            super(PHONE_CODE_NOT_FOUND);
        }
    }
    public static class SessionHasNotEmailException extends AuthException {

        public SessionHasNotEmailException() {
            super(SESSION_HAS_NOT_EMAIL);
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
    public static class GoToLoginException extends AuthException{

        public GoToLoginException() {
            super(GO_TO_LOGIN);
        }
    }
    public static class LoginRequiredException extends AuthException {

        public LoginRequiredException(AuthenticationException e) {
            super(LOGIN_REQUIRED, e);
        }
        public LoginRequiredException() {
            super(LOGIN_REQUIRED);
        }
    }

    public static class ForbiddenException extends AuthException {

        public ForbiddenException(AccessDeniedException e) {
            super(FORBIDDEN, e);
        }

        public ForbiddenException() {
            this(null);
        }
    }
}

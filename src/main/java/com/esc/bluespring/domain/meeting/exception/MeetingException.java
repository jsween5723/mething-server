package com.esc.bluespring.domain.meeting.exception;

import com.esc.bluespring.common.exception.ApplicationException;
import com.esc.bluespring.common.exception.ExceptionCode;

public class MeetingException extends ApplicationException {

    protected MeetingException(ExceptionCode exceptionCode) {
        super(exceptionCode, null);
    }

    static public class MeetingNotFoundException extends MeetingException {

        public MeetingNotFoundException() {
            super(MeetingExceptionCode.MEETING_NOT_FOUND);
        }
    }
}

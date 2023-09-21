package com.esc.bluespring.domain.meeting.request.exception;

import com.esc.bluespring.common.exception.ApplicationException;
import com.esc.bluespring.common.exception.ExceptionCode;

public class MeetingRequestException extends ApplicationException {

    protected MeetingRequestException(ExceptionCode exceptionCode) {
        super(exceptionCode);
    }

    static public class MeetingRequestNotFoundRequestException extends MeetingRequestException {

        public MeetingRequestNotFoundRequestException() {
            super(MeetingRequestExceptionCode.MEETING_REQUEST_NOT_FOUND);
        }
    }
}

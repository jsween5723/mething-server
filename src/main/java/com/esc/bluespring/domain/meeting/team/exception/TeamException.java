package com.esc.bluespring.domain.meeting.team.exception;

import com.esc.bluespring.common.exception.ApplicationException;
import com.esc.bluespring.common.exception.ExceptionCode;

public class TeamException extends ApplicationException {

    protected TeamException(ExceptionCode exceptionCode) {
        super(exceptionCode, null);
    }

    static public class TeamNotFoundException extends TeamException {

        public TeamNotFoundException() {
            super(TeamExceptionCode.TEAM_NOT_FOUND);
        }
    }
}

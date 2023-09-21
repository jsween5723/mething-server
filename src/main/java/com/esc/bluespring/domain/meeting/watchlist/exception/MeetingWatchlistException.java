package com.esc.bluespring.domain.meeting.watchlist.exception;

import com.esc.bluespring.common.exception.ApplicationException;
import com.esc.bluespring.common.exception.ExceptionCode;

public class MeetingWatchlistException extends ApplicationException {

    protected MeetingWatchlistException(ExceptionCode exceptionCode) {
        super(exceptionCode);
    }

    static public class MeetingWatchlistItemNotFoundException extends MeetingWatchlistException {

        public MeetingWatchlistItemNotFoundException() {
            super(MeetingWatchlistExceptionCode.MEETING_WATCHLIST_ITEM_NOT_FOUND);
        }
    }
}

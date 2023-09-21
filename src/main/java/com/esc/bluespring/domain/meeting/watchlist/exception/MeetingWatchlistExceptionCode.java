package com.esc.bluespring.domain.meeting.watchlist.exception;

import com.esc.bluespring.common.exception.ExceptionCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
enum MeetingWatchlistExceptionCode implements ExceptionCode {
    MEETING_WATCHLIST_ITEM_NOT_FOUND(HttpStatus.NOT_FOUND, "관심 목록에서 해당 미팅을 찾을 수 없습니다.");
    private final HttpStatus httpStatus;
    private final String message;

    @Override
    public String getCode() {
        return name();
    }
}

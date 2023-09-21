package com.esc.bluespring.domain.meeting.request.exception;

import com.esc.bluespring.common.exception.ExceptionCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
enum MeetingRequestExceptionCode implements ExceptionCode {
    MEETING_REQUEST_NOT_FOUND(HttpStatus.NOT_FOUND, "미팅 신청을 찾을 수 없습니다.");
    private final HttpStatus httpStatus;
    private final String message;

    @Override
    public String getCode() {
        return name();
    }
}

package com.esc.bluespring.common;

import com.esc.bluespring.common.exception.ErrorResponse;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BaseResponse<T> {
    private final Integer status;
    private final String successId;
    private final T data;
    private final String errorId;
    private final String message;

    public BaseResponse(ErrorResponse error) {
        this.status = error.getStatus().value();
        this.data = null;
        this.successId = null;
        this.errorId = error.getCode();
        this.message = error.getMessage();
    }

    public BaseResponse(T data) {
        this.status = 200;
        this.data = data;
        this.successId = "OK";
        this.errorId = null;
        this.message = null;
    }
}

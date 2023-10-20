package com.esc.bluespring.common;

import com.esc.bluespring.common.exception.ErrorResponse;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BaseResponse<T> {
    private final boolean success;
    private final T data;
    private final ErrorResponse error;

    public BaseResponse(ErrorResponse error) {
        this.success = false;
        this.data = null;
        this.error = error;
    }

    public BaseResponse(T data) {
        this.data = data;
        this.success = true;
        this.error = null;
    }
}

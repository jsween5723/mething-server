package com.esc.bluespring.common.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum RequestStatus {
    PENDING, ACCEPTED, REJECTED;

    @JsonCreator
    public RequestStatus toEnum(String name) {
        try {
            return RequestStatus.valueOf(name);
        } catch (Exception ignored) {}
        return null;
    }

    @JsonValue
    public String toValue() {
        return name();
    }
}

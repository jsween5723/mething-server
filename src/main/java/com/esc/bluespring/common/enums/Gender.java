package com.esc.bluespring.common.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Gender {
    MALE, FEMALE;

    @JsonCreator
    public Gender toEnum(String name) {
        try {
            return Gender.valueOf(name);
        } catch (Exception ignored) {}
        return null;
    }

    @JsonValue
    public String toValue() {
        return name();
    }
}

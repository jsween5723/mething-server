package com.esc.bluespring.common.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum MBTI {
    INFP,INFJ,INTP,INTJ,ISFP,ISFJ,ISTP,ISTJ,
    ENFP,ENFJ,ENTP,ENTJ,ESFP,ESFJ,ESTP,ESTJ
    ;

    @JsonCreator
    public MBTI toEnum(String name) {
        try {
            return MBTI.valueOf(name);
        } catch (Exception ignored) {}
        return null;
    }

    @JsonValue
    public String toValue() {
        return name();
    }
}

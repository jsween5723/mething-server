package com.esc.bluespring.domain.member.entity.profile;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum Sport {
    ALLDAYS("매일"), OFTEN("자주"), SOMETIMES("가끔"), NOT("안 함");
    public final String name;
    @JsonValue
    public String toJson() {
        return name;
    }
}

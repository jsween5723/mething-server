package com.esc.bluespring.domain.member.entity.profile;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum BioPattern {
    MORNING("아침형 인간"), NIGHT("저녁형 인간");
    public final String name;
    @JsonValue
    public String toJson() {
        return name;
    }
}

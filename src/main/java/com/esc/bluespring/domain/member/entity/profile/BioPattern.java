package com.esc.bluespring.domain.member.entity.profile;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum BioPattern {
    MORNING("아침형 인간"), NIGHT("저녁형 인간");
    public final String name;
}

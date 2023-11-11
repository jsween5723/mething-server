package com.esc.bluespring.domain.member.entity.profile;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum Smoke {
    YES("흡연"), NO("비흡연"), NO_TRY("금연 중");
    public final String name;
}

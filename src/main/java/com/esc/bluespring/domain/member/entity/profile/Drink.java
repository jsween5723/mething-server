package com.esc.bluespring.domain.member.entity.profile;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum Drink {
    ALLDAYS("매일"), OFTEN("자주"), SOMETIMES("가끔"), NOT("안 함");
    public final String name;
}

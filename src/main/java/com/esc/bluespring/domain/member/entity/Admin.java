package com.esc.bluespring.domain.member.entity;

import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Admin extends Member {

    public Admin(Long id, String email, String password) {
        super(id, email, password);
    }

    @Override
    public Role getRole() {
        return Role.ADMIN;
    }

}

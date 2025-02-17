package com.esc.bluespring.domain.member.entity;

import jakarta.persistence.Entity;
import java.util.List;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Admin extends Member {

    public Admin(UUID id, String email, String password) {
        super(id, email, password);
    }

    @Override
    public List<Role> getRole() {
        return List.of(Role.ADMIN);
    }

    @Override
    public void valid() {

    }

}

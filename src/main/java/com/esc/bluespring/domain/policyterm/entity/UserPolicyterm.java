package com.esc.bluespring.domain.policyterm.entity;

import com.esc.bluespring.common.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user_policyterms")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserPolicyterm extends BaseEntity {
    private String title;
    private boolean required;
    @Column(length = 1000)
    private String link;

    public UserPolicyterm(UUID id) {
        super(id);
    }
}

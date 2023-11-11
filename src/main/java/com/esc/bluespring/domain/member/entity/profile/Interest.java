package com.esc.bluespring.domain.member.entity.profile;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;

@Entity
@Table(name = "interests")
@Getter
public class Interest {
    @Id
    private String name;
}

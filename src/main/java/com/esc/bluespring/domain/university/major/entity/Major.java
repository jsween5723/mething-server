package com.esc.bluespring.domain.university.major.entity;

import com.esc.bluespring.common.entity.InformationEntity;
import com.esc.bluespring.domain.university.entity.University;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "majors", indexes = {@Index(name = "name_index", columnList = "name")})
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Major extends InformationEntity {

    private String name;

    @JoinColumn(name = "university_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private University university;

    @Builder
    public Major(Long id, LocalDateTime createdAt, LocalDateTime updatedAt, String name,
        University university) {
        super(id, createdAt, updatedAt);
        this.name = name;
        this.university = university;
    }

    public void changeUniversity(University university) {
        this.university = university;
    }

}

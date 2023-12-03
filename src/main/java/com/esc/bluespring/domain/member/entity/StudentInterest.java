package com.esc.bluespring.domain.member.entity;

import com.esc.bluespring.common.entity.BaseEntity;
import com.esc.bluespring.domain.member.entity.profile.Interest;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "student_interests")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StudentInterest extends BaseEntity {

    @JoinColumn
    @ManyToOne(fetch = FetchType.LAZY)
    Student student;
    @JoinColumn
    @ManyToOne(fetch = FetchType.LAZY)
    Interest interest;

    public StudentInterest(Student student, Interest interest) {
        this.student = student;
        this.interest = interest;
    }

    void assignStudent(Student student) {
        this.student = student;
    }
}

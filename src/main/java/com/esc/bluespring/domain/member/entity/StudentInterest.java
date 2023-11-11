package com.esc.bluespring.domain.member.entity;

import com.esc.bluespring.domain.member.entity.profile.Interest;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "student_interests")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StudentInterest {

    @EmbeddedId
    private StudentInterestPK pk;

    @MapsId("student_id")
    @ManyToOne(fetch = FetchType.LAZY)
    Student student;
    @MapsId("interest_name")
    @ManyToOne(fetch = FetchType.LAZY)
    Interest interest;

    public StudentInterest(Student student, Interest interest) {
        this.student = student;
        this.interest = interest;
    }

    void assignStudent(Student student) {
        this.student = student;
    }

    @Getter
    @AllArgsConstructor
    public static class StudentInterestPK implements Serializable {
        @Column(name = "interest_name")
        private String interestName;
        @Column(name = "student_id")
        private UUID studentId;
    }
}

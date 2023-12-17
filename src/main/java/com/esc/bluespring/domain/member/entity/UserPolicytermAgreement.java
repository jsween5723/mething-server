package com.esc.bluespring.domain.member.entity;

import com.esc.bluespring.common.entity.BaseEntity;
import com.esc.bluespring.domain.policyterm.entity.UserPolicyterm;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;

@Entity
@Table(name = "user_policyterm_agreements")
@Getter
public class UserPolicytermAgreement extends BaseEntity {
    @JoinColumn(name = "student_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Student student;
    @JoinColumn(name = "user_policyterm_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private UserPolicyterm policyterm;

    public UserPolicytermAgreement(Student student, UserPolicyterm policyterm) {
        this.student = student;
        this.policyterm = policyterm;
    }
}

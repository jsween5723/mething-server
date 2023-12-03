package com.esc.bluespring.domain.member.entity;

import com.esc.bluespring.common.enums.MBTI;
import com.esc.bluespring.domain.member.entity.profile.BioPattern;
import com.esc.bluespring.domain.member.entity.profile.Drink;
import com.esc.bluespring.domain.member.entity.profile.Smoke;
import com.esc.bluespring.domain.member.entity.profile.Sport;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.OneToMany;
import java.util.LinkedHashSet;
import java.util.Set;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StudentProfile {

    @Enumerated(EnumType.STRING)
    private Drink drink;
    @Enumerated(EnumType.STRING)
    private Smoke smoke;
    @Enumerated(EnumType.STRING)
    private Sport sport;
    @Enumerated(EnumType.STRING)
    private BioPattern bioPattern;
    @Enumerated(EnumType.STRING)
    private MBTI mbti;
    private Double stature;
    @OneToMany(mappedBy = "student", cascade = CascadeType.PERSIST)
    private Set<StudentInterest> interests = new LinkedHashSet<>();

    public StudentProfile(Drink drink, Smoke smoke, Sport sport, BioPattern bioPattern, MBTI mbti,
                          Set<StudentInterest> interests, Double stature) {
        this.drink = drink;
        this.smoke = smoke;
        this.sport = sport;
        this.bioPattern = bioPattern;
        this.mbti = mbti;
        this.interests = interests;
        this.stature = stature;
    }

    void assignStudent(Student student) {
        interests.forEach(studentInterest -> studentInterest.assignStudent(student));
    }
}

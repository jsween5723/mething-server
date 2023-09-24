package com.esc.bluespring.domain.member.entity;

import com.esc.bluespring.domain.file.entity.Image;
import com.esc.bluespring.domain.university.major.entity.Major;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SchoolInformation {
    @JoinColumn(name = "major_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Major major;
    @JoinColumn(name = "student_certification_image_url", referencedColumnName = "url")
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Image studentCertificationImageUrl;
    private boolean isCertificated = false;
    @Column(nullable = false)
    private String name;

    public SchoolInformation(Major major, Image studentCertificationImageUrl,
        Boolean isCertificated, String name) {
        this.name = name;
        this.major = major;
        this.studentCertificationImageUrl = studentCertificationImageUrl;
        this.isCertificated = isCertificated != null && isCertificated;
    }
}

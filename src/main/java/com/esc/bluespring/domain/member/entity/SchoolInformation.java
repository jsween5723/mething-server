package com.esc.bluespring.domain.member.entity;

import com.esc.bluespring.domain.file.entity.Image;
import com.esc.bluespring.domain.member.entity.profile.CertificateStatus;
import com.esc.bluespring.domain.university.major.entity.Major;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
  @ManyToOne(fetch = FetchType.EAGER)
  private Major major;
  @JoinColumn(name = "student_certification_image_url", referencedColumnName = "url")
  @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
  private Image studentCertificationImage;
  @Column(name = "student_certification_image_url", insertable = false, updatable = false)
  private String studentCertificationImageUrl;
  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private CertificateStatus certificateStatus = CertificateStatus.NOT_ASSIGNED;
  @Column(nullable = false)
  private String name;
  void reassignCertificationImage(Image image) {
    this.studentCertificationImage = image;
    certificateStatus = CertificateStatus.PENDING;
  }
  public SchoolInformation(Major major, Image studentCertificationImage, CertificateStatus certificateStatus,
                           String name) {
    this.name = name;
    this.major = major;
    this.studentCertificationImage = studentCertificationImage;
    this.certificateStatus = certificateStatus != null ? certificateStatus : CertificateStatus.NOT_ASSIGNED;
  }

  void patch(SchoolInformation source) {
    if (source.major == null && source.name == null && source.studentCertificationImage == null) {
      return;
    }
    if (source.major != null) {
      major = source.major;
    }
    if (source.name != null && !source.name.isBlank()) {
      name = source.name;
    }
    if (source.studentCertificationImage != null) {
      studentCertificationImage = source.studentCertificationImage;
    }
    changeCertificationState(studentCertificationImage != null ? CertificateStatus.PENDING : CertificateStatus.NOT_ASSIGNED);
  }

  public void changeCertificationState(CertificateStatus state) {
    certificateStatus = state;
  }
}

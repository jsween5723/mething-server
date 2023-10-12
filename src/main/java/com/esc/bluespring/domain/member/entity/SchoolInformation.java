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
  @ManyToOne(fetch = FetchType.EAGER)
  private Major major;
  @JoinColumn(name = "student_certification_image_url", referencedColumnName = "url")
  @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
  private Image studentCertificationImage;
  @Column(name = "student_certification_image_url", insertable = false, updatable = false)
  private String studentCertificationImageUrl;
  private boolean isCertificated = false;
  @Column(nullable = false)
  private String name;

  public SchoolInformation(Major major, Image studentCertificationImage, Boolean isCertificated,
                           String name) {
    this.name = name;
    this.major = major;
    this.studentCertificationImage = studentCertificationImage;
    this.isCertificated = isCertificated != null && isCertificated;
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
    changeCertificationState(false);
  }

  public void changeCertificationState(boolean state) {
    isCertificated = state;
  }
}

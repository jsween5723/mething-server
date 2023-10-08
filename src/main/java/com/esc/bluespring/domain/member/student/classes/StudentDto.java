package com.esc.bluespring.domain.member.student.classes;

import com.esc.bluespring.domain.university.major.classes.MajorDto;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record StudentDto() {

  public record SearchCondition(Boolean isCertificated, String name, Long majorId,
                                Long universityId, String nickname, String email) {

  }

  public record TeamListElement(UUID id, String introduce, String profileImageUrl, SchoolInformation schoolInformation,
                                String nickname) {

  }

  public record Detail(UUID id, String email, String introduce, String profileImageUrl,
                       SchoolInformationForDetail schoolInformation, String nickname,
                       Long createdAt) {

  }

  public record SchoolInformation(String name, MajorDto.SearchListElement major,
                                  boolean isCertificated) {

  }

  public record SchoolInformationForDetail(String name, MajorDto.SearchListElement major,
                                           String studentCertificationImageUrl,
                                           boolean isCertificated) {

  }

  public record ChangeCertificationState(@NotNull Boolean certificationState) {

  }
}

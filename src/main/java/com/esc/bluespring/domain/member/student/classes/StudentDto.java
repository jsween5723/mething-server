package com.esc.bluespring.domain.member.student.classes;

import com.esc.bluespring.domain.member.classes.MemberDto.ProfileDto;
import com.esc.bluespring.domain.university.major.classes.MajorDto;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record StudentDto() {

  public record AdminStudentSearchCondition(Boolean isCertificated, String name, Long majorId,
                                            Long universityId, String nickname, String email) {

  }

  public record StudentSearchCondition(String nickname) {

  }

  public record ListElement(UUID id, String introduce, String profileImageUrl, SchoolInformation schoolInformation,
                            String nickname, Integer birthYear) { }

  public record SchoolInformation(String name, MajorDto.SearchListElement major,
                                  boolean isCertificated) {

  }

  public record ChangeCertificationState(@NotNull Boolean certificationState) {

  }

  public record ChangeCertificationImage(@NotNull String certificationImageUrl) {

  }

  public record DetailResponse(UUID id, String introduce, String profileImageUrl, SchoolInformation schoolInformation,
                               String nickname, ProfileDto profile, Integer birthYear) {}
}

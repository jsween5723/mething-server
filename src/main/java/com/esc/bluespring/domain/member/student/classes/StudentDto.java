package com.esc.bluespring.domain.member.student.classes;

import com.esc.bluespring.domain.university.major.classes.MajorDto;
import jakarta.validation.constraints.NotNull;

public record StudentDto() {

    public record SearchCondition(Boolean isCertificated, String name, Long majorId,
                                  Long universityId, String nickname, String email) {

    }

    public record SchoolInformationListElement(Long id, String name,
                                               MajorDto.SearchListElement major, String nickname,
                                               long createdAt, String email,
                                               String studentCertificationImageUrl,
                                               boolean isCertificated) {

    }

    public record ChangeCertificationState(@NotNull boolean certificationState) {

    }
}

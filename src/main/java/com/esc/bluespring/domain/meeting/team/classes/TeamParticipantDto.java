package com.esc.bluespring.domain.meeting.team.classes;

import com.esc.bluespring.domain.member.student.classes.StudentDto.SchoolInformation;
import java.util.UUID;

public record TeamParticipantDto() {
    public record MainPageListElement(UUID id, String introduce, String profileImageUrl, SchoolInformation schoolInformation,
                                      String nickname, boolean isOwner, Integer birthYear) {}
}
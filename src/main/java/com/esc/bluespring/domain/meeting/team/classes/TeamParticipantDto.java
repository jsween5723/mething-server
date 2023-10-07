package com.esc.bluespring.domain.meeting.team.classes;

import com.esc.bluespring.domain.member.student.classes.StudentDto.TeamListElement;
import java.util.UUID;

public record TeamParticipantDto() {
    public record MainPageListElement(UUID id, TeamListElement student, boolean isOwner) {}
}

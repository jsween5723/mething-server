package com.esc.bluespring.domain.meeting.team.classes;

import com.esc.bluespring.domain.university.major.classes.MajorDto;
import java.util.UUID;

public record TeamParticipantDto() {
    public record MainPageListElement(UUID id, String profileImageUrl, MajorDto.SearchListElement major, int age, boolean isOwner) {}
}

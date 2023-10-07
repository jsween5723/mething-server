package com.esc.bluespring.domain.meeting.team.classes;

import com.esc.bluespring.domain.university.classes.UniversityDto;
import java.util.Set;
import java.util.UUID;

public record TeamDto() {

    public record MainPageListElement(UUID id, String title, Integer maxParticipantNumber,
                                      UniversityDto.MainPageListElement representedUniversity,
                                      Set<TeamParticipantDto.MainPageListElement> participants, boolean isOwner) {
    }
}

package com.esc.bluespring.domain.meeting.team.classes;

import com.esc.bluespring.domain.university.classes.UniversityDto;
import java.util.List;
import java.util.UUID;

public record TeamDto() {

    public record MainPageListElement(UUID id, String title, Integer maxParticipantNumber,
                                      UniversityDto.MainPageListElement representedUniversity,
                                      List<TeamParticipantDto.MainPageListElement> participants, boolean isOwner) {
    }
}

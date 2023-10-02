package com.esc.bluespring.domain.meeting.team.classes;

import com.esc.bluespring.domain.university.classes.UniversityDto;
import java.util.List;

public record TeamDto() {

    public record MainPageListElement(Long id, String title, Integer maxParticipantNumber,
                                      UniversityDto.MainPageListElement representedUniversity,
                                      List<TeamParticipantDto.MainPageListElement> participants) {

    }
}

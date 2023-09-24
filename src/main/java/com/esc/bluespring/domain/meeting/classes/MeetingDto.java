package com.esc.bluespring.domain.meeting.classes;

import com.esc.bluespring.domain.meeting.team.classes.TeamDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;
import org.hibernate.validator.constraints.Length;

public record MeetingDto() {

    public record SearchCondition(boolean isMyLocation) {

    }

    public record MainPageListElement(Long id, TeamDto.MainPageListElement fromTeam, Long count, Boolean isAdded,
                                      Long createdAt) {
    }

    public record Create(@NotNull @NotBlank @Length(max = 12) String title,
                         @NotNull @NotBlank @Length(max = 80) String introduce,
                         @NotNull Long representedUniversityId,
                         @Size(max = 3) List<Long> participantIds) {

    }

    public record Request(@NotNull @NotBlank @Length(max = 12) String title,
                          @NotNull @NotBlank @Length(max = 50) String message,
                          @NotNull Long representedUniversityId,
                          @Size(max = 3) List<Long> participantIds) {

    }
}

package com.esc.bluespring.domain.meeting.classes;

import com.esc.bluespring.domain.meeting.team.classes.TeamDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;
import java.util.UUID;
import org.hibernate.validator.constraints.Length;

public record MeetingDto() {

    public record MainPageSearchCondition(boolean isMyLocation) {

    }


    public record MainPageListElement(UUID id, String introduce,
                                      TeamDto.MainPageListElement ownerTeam, Integer likeCount,
                                      Boolean isLiked, Long createdAt) {

    }

    public record Detail(Integer likeCount, Boolean isLiked, UUID id, String introduce, Integer requestCount,
                         TeamDto.MainPageListElement ownerTeam,
                         TeamDto.MainPageListElement engagedTeam, Long createdAt) {

    }

    public record MyMeetingPageListElement(UUID id, TeamDto.MainPageListElement myTeam,
                                           Integer requestCount, Long createdAt) {

    }

    public record Create(@NotNull @NotBlank @Length(max = 12) String title,
                         @NotNull @NotBlank @Length(max = 80) String introduce,
                         @NotNull Long representedUniversityId,
                         @Size(max = 3) List<UUID> participantIds) {

    }

    public record Request(@NotNull @NotBlank @Length(max = 12) String title,
                          @NotNull @NotBlank @Length(max = 50) String message,
                          @NotNull Long representedUniversityId,
                          @Size(max = 3) List<UUID> participantIds) {

    }
}

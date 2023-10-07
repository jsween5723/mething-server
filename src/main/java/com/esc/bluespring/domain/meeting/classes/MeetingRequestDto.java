package com.esc.bluespring.domain.meeting.classes;

import com.esc.bluespring.common.enums.RequestStatus;
import com.esc.bluespring.domain.meeting.team.classes.TeamDto;
import java.util.UUID;

public record MeetingRequestDto() {

  public record MeetingDetailRequestListElement(UUID id, String message, TeamDto.MainPageListElement requesterTeam,
                                                RequestStatus status, Long createdAt) {

  }

  public record ListElement(UUID id, String message, MeetingDto.MainPageListElement targetMeeting,
                            RequestStatus status, Long createdAt) {
    
  }

  public record Detail(UUID id, String message, TeamDto.MainPageListElement requesterTeam,
                       MeetingDto.MainPageListElement targetMeeting, RequestStatus status,
                       Long createdAt) {
    
  }
}

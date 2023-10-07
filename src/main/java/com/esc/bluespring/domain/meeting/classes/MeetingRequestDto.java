package com.esc.bluespring.domain.meeting.classes;

import com.esc.bluespring.common.enums.RequestStatus;
import com.esc.bluespring.domain.meeting.team.classes.TeamDto;
import java.util.Set;
import java.util.UUID;

public record MeetingRequestDto() {
  public enum Field{
    requesterTeam, targetMeeting
  }
  public record SearchCondition(Set<RequestStatus> statuses, Set<Field> fields){}
  public record Detail(UUID id, String message, TeamDto.MainPageListElement requesterTeam,
                       MeetingDto.MainPageListElement targetMeeting, RequestStatus status,
                       Long createdAt) {
    
  }
}

package com.esc.bluespring.domain.meeting.mapper;

import com.esc.bluespring.domain.locationDistrict.LocationDistrictMapper;
import com.esc.bluespring.domain.meeting.entity.TeamParticipant;
import com.esc.bluespring.domain.meeting.team.classes.TeamParticipantDto.MainPageListElement;
import com.esc.bluespring.domain.member.classes.MemberMapper;
import com.esc.bluespring.domain.member.entity.Student;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {MemberMapper.class, LocationDistrictMapper.class})
public interface TeamParticipantMapper {

  TeamParticipantMapper INSTANCE = Mappers.getMapper(TeamParticipantMapper.class);

  default MainPageListElement toMainPageListElement(TeamParticipant participant) {
    return toMainPageListElement(participant.getMember(), false);
  }

  @Mapping(target = "student", source = "member")
  MainPageListElement toMainPageListElement(Student member, boolean isOwner);

  default Set<MainPageListElement> participants(Student owner, Set<TeamParticipant> participants) {
    Set<MainPageListElement> result =
        participants.stream().map(this::toMainPageListElement).collect(Collectors.toSet());
    result.add(toMainPageListElement(owner, true));
    return result;
  }

  @Mapping(target = "member", source = "id")
  TeamParticipant toEntityFromMemberId(UUID id);
}

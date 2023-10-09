package com.esc.bluespring.domain.meeting.mapper;

import com.esc.bluespring.common.LazyLoadingAwareMapper;
import com.esc.bluespring.domain.locationDistrict.LocationDistrictMapper;
import com.esc.bluespring.domain.meeting.entity.TeamParticipant;
import com.esc.bluespring.domain.meeting.team.classes.TeamParticipantDto.MainPageListElement;
import com.esc.bluespring.domain.member.classes.MemberMapper;
import com.esc.bluespring.domain.member.entity.Student;
import java.util.Set;
import java.util.UUID;
import org.mapstruct.Condition;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {MemberMapper.class, LocationDistrictMapper.class})
public interface TeamParticipantMapper extends LazyLoadingAwareMapper {

  @Condition
  default boolean isNotLazyLoadedParticipant(Set<TeamParticipant> sourceCollection) {
    return LazyLoadingAwareMapper.super.isNotLazyLoaded(sourceCollection);
  }

  TeamParticipantMapper INSTANCE = Mappers.getMapper(TeamParticipantMapper.class);

  default MainPageListElement toMainPageListElement(TeamParticipant participant) {
    return toMainPageListElement(participant.getParticipant(), false);
  }

  MainPageListElement toMainPageListElement(Student member, boolean isOwner);

  Set<MainPageListElement> participants(Set<TeamParticipant> participants);

  @Mapping(target = "participant", source = "id")
  TeamParticipant toEntityFromMemberId(UUID id);
}

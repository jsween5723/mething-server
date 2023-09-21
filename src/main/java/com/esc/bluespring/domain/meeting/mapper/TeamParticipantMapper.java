package com.esc.bluespring.domain.meeting.mapper;

import com.esc.bluespring.domain.meeting.team.classes.TeamParticipantDto.MainPageListElement;
import com.esc.bluespring.domain.meeting.team.entity.TeamParticipant;
import com.esc.bluespring.domain.member.classes.StudentMapper;
import com.esc.bluespring.domain.member.entity.Student;
import java.util.List;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants.ComponentModel;

@Mapper(uses = {StudentMapper.class}, injectionStrategy = InjectionStrategy.CONSTRUCTOR, componentModel = ComponentModel.SPRING)
public interface TeamParticipantMapper {
    default MainPageListElement toMainPageListElement(TeamParticipant participant) {
        return toMainPageListElement(participant.getMember(), false);
    }

    @Mapping(target = "profileImageUrl", source = "member.profileImage.url")
    MainPageListElement toMainPageListElement(Student member, boolean isOwner);

    List<TeamParticipant> toEntities(List<Long> ids);

    @Mapping(target = "member", source = "id")
    TeamParticipant toEntity(Long id);
}

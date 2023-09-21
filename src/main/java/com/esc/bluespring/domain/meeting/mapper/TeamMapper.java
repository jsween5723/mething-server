package com.esc.bluespring.domain.meeting.mapper;

import com.esc.bluespring.domain.meeting.classes.MeetingDto.Create;
import com.esc.bluespring.domain.meeting.classes.MeetingDto.Request;
import com.esc.bluespring.domain.meeting.team.classes.TeamDto.MainPageListElement;
import com.esc.bluespring.domain.meeting.team.classes.TeamParticipantDto;
import com.esc.bluespring.domain.meeting.team.entity.MeetingOwnerTeam;
import com.esc.bluespring.domain.meeting.team.entity.MeetingRequesterTeam;
import com.esc.bluespring.domain.member.entity.Student;
import com.esc.bluespring.domain.university.classes.UniversityMapper;
import com.esc.bluespring.domain.university.entity.University;
import java.util.List;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants.ComponentModel;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {UniversityMapper.class,
    TeamParticipantMapper.class}, injectionStrategy = InjectionStrategy.CONSTRUCTOR, componentModel = ComponentModel.SPRING)
public interface TeamMapper {
    default MainPageListElement toMainPageListElement(MeetingOwnerTeam team) {
        TeamParticipantMapper teamParticipantMapper = Mappers.getMapper(TeamParticipantMapper.class);
        TeamParticipantDto.MainPageListElement owner = teamParticipantMapper.toMainPageListElement(
            (Student) team.getOwner(), true);
        List<TeamParticipantDto.MainPageListElement> participants = new java.util.ArrayList<>(
            team.getParticipants().stream()
                .map(teamParticipantMapper::toMainPageListElement).toList());
        participants.add(owner);
        return toMainPageListElement(team.getId(), team.getTitle(), team.getIntroduce(),
            team.getRepresentedUniversity(), participants);
    }

    MainPageListElement toMainPageListElement(Long id, String title, String introduce,
        University representedUniversity,
        List<TeamParticipantDto.MainPageListElement> participants);


    MeetingOwnerTeam toEntity(Long id);

    @Mapping(target = "representedUniversity", source = "dto.representedUniversityId")
    @Mapping(target = "participants", source = "dto.participantIds")
    @Mapping(target = "owner", source = "owner")
    MeetingOwnerTeam toEntity(Create dto, Student owner);

    @Mapping(target = "representedUniversity", source = "dto.representedUniversityId")
    @Mapping(target = "participants", source = "dto.participantIds")
    @Mapping(target = "owner", source = "owner")
    MeetingRequesterTeam toEntity(Request dto, Student owner);
}

package com.esc.bluespring.domain.meeting.mapper;

import com.esc.bluespring.domain.meeting.classes.MeetingDto.Create;
import com.esc.bluespring.domain.meeting.classes.MeetingDto.Request;
import com.esc.bluespring.domain.meeting.entity.MeetingOwnerTeam;
import com.esc.bluespring.domain.meeting.entity.MeetingRequesterTeam;
import com.esc.bluespring.domain.meeting.entity.Team;
import com.esc.bluespring.domain.meeting.team.classes.TeamDto.MainPageListElement;
import com.esc.bluespring.domain.member.entity.Student;
import com.esc.bluespring.domain.university.classes.UniversityMapper;
import java.util.UUID;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {TeamParticipantMapper.class, UniversityMapper.class})
public interface TeamMapper {

    TeamMapper INSTANCE = Mappers.getMapper(TeamMapper.class);
    TeamParticipantMapper participantMapper = Mappers.getMapper(TeamParticipantMapper.class);

    @Mapping(target = "isOwner", expression = "java(team.isOwner())")
    @Mapping(target = "participants", expression = "java(participantMapper.participants((Student) team.getOwner(), team.getParticipants()))")
    MainPageListElement toMainPageListElement(Team team);


    MeetingOwnerTeam toEntity(UUID id);

    @Mapping(target = "representedUniversity", source = "dto.representedUniversityId")
    @Mapping(target = "owner", source = "owner")
    @Mapping(target = "participants", source = "dto.participantIds")
    MeetingOwnerTeam toEntity(Create dto, Student owner);

    @Mapping(target = "representedUniversity", source = "dto.representedUniversityId")
    @Mapping(target = "participants", source = "dto.participantIds")
    @Mapping(target = "owner", source = "owner")
    MeetingRequesterTeam toEntity(Request dto, Student owner);
}

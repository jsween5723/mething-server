package com.esc.bluespring.domain.meeting.mapper;

import com.esc.bluespring.domain.meeting.classes.MeetingDto.Create;
import com.esc.bluespring.domain.meeting.classes.MeetingDto.Request;
import com.esc.bluespring.domain.meeting.team.classes.TeamDto.MainPageListElement;
import com.esc.bluespring.domain.meeting.team.classes.TeamParticipantDto;
import com.esc.bluespring.domain.meeting.entity.MeetingOwnerTeam;
import com.esc.bluespring.domain.meeting.entity.MeetingRequesterTeam;
import com.esc.bluespring.domain.member.entity.Student;
import com.esc.bluespring.domain.university.classes.UniversityMapper;
import com.esc.bluespring.domain.university.entity.University;
import java.util.List;
import java.util.UUID;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {TeamParticipantMapper.class, UniversityMapper.class})
public interface TeamMapper {

    TeamMapper INSTANCE = Mappers.getMapper(TeamMapper.class);
    TeamParticipantMapper participantMapper = Mappers.getMapper(TeamParticipantMapper.class);

    default MainPageListElement toMainPageListElement(MeetingOwnerTeam team) {
        TeamParticipantDto.MainPageListElement owner = TeamParticipantMapper.INSTANCE.toMainPageListElement(
            (Student) team.getOwner(), true);
        List<TeamParticipantDto.MainPageListElement> participants = new java.util.ArrayList<>(
            team.getParticipants().stream()
                .map(TeamParticipantMapper.INSTANCE::toMainPageListElement).toList());
        participants.add(owner);
        return toMainPageListElement(team.getId(), team.getTitle(), team.getRepresentedUniversity(),
            participants);
    }

    @Mapping(target = "representedUniversity", source = "representedUniversity")
    MainPageListElement toMainPageListElement(UUID id, String title,
        University representedUniversity,
        List<TeamParticipantDto.MainPageListElement> participants);


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

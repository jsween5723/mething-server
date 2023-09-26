package com.esc.bluespring.domain.meeting.mapper;

import com.esc.bluespring.common.entity.OwnerEntity;
import com.esc.bluespring.common.utils.time.TimeMapper;
import com.esc.bluespring.domain.meeting.classes.MeetingDto.Create;
import com.esc.bluespring.domain.meeting.classes.MeetingDto.MainPageListElement;
import com.esc.bluespring.domain.meeting.classes.MeetingDto.MyMeetingPageListElement;
import com.esc.bluespring.domain.meeting.entity.Meeting;
import com.esc.bluespring.domain.meeting.team.classes.TeamDto;
import com.esc.bluespring.domain.meeting.team.entity.MeetingOwnerTeam;
import com.esc.bluespring.domain.meeting.team.entity.Team;
import com.esc.bluespring.domain.member.entity.Student;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {TimeMapper.class, TeamMapper.class})
public interface MeetingMapper {
    MeetingMapper INSTANCE = Mappers.getMapper(MeetingMapper.class);
    TeamMapper teamMapper = Mappers.getMapper(TeamMapper.class);
    @Mapping(target = "likeCount", expression = "java(meeting.getWatchlist().size())")
    @Mapping(target = "isLiked", expression = "java(toIsLiked(meeting, student))")
    @Mapping(target = "id", source = "meeting.id")
    @Mapping(target = "createdAt", source = "meeting.createdAt")
    @Mapping(target = "fromTeam", source = "meeting.fromTeam")
    MainPageListElement toMainPageListElement(Meeting meeting, Student student);

    @Mapping(target = "requestCount", source = "meeting")
    MyMeetingPageListElement toMyMeetingPageListElement(Meeting meeting);

    default Integer getRequestCount(Meeting meeting) {
        return meeting.getJoinRequests().size();
    }

    default boolean toIsLiked(Meeting meeting, Student student) {
        return meeting.getWatchlist().stream().map(OwnerEntity::getOwner).toList()
            .contains(student);
    }

    default TeamDto.MainPageListElement toFromTeam(Team fromTeam) {
        return teamMapper.toMainPageListElement((MeetingOwnerTeam) fromTeam);
    }

    @Mapping(target = "fromTeam", expression = "java(teamMapper.toEntity(dto,owner))")
    @Mapping(target = "watchlist", ignore = true)
    Meeting toEntity(Create dto, Student owner);


}

package com.esc.bluespring.domain.meeting.mapper;

import com.esc.bluespring.common.entity.OwnerEntity;
import com.esc.bluespring.common.utils.time.TimeMapper;
import com.esc.bluespring.domain.meeting.classes.MeetingDto.Create;
import com.esc.bluespring.domain.meeting.classes.MeetingDto.MainPageListElement;
import com.esc.bluespring.domain.meeting.classes.MeetingDto.MyMeetingPageListElement;
import com.esc.bluespring.domain.meeting.entity.Meeting;
import com.esc.bluespring.domain.member.entity.Student;
import com.esc.bluespring.domain.university.classes.UniversityMapper;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants.ComponentModel;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {UniversityMapper.class, TimeMapper.class,
    TeamMapper.class}, injectionStrategy = InjectionStrategy.CONSTRUCTOR, componentModel = ComponentModel.SPRING)
public interface MeetingMapper {

    TeamMapper teamMapper = Mappers.getMapper(TeamMapper.class);

    @Mapping(target = "likeCount", expression = "java(meeting.getWatchlist().size())")
    @Mapping(target = "isLiked", expression = "java(toIsAdded(meeting, student))")
    @Mapping(target = "id", source = "meeting.id")
    @Mapping(target = "createdAt", source = "meeting.createdAt")
    MainPageListElement toMainPageListElement(Meeting meeting, Student student);

    @Mapping(target = "requestCount", source = "meeting")
    MyMeetingPageListElement toMyMeetingPageListElement(Meeting meeting);

    default Integer getRequestCount(Meeting meeting) {
        return meeting.getJoinRequests().size();
    }
    default boolean toIsAdded(Meeting meeting, Student student) {
        return meeting.getWatchlist().stream().map(OwnerEntity::getOwner).toList()
            .contains(student);
    }

    @Mapping(target = "fromTeam", expression = "java(teamMapper.toEntity(dto,owner))")
    Meeting toEntity(Create dto, Student owner);


}

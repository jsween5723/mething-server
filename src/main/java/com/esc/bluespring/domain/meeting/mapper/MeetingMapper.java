package com.esc.bluespring.domain.meeting.mapper;

import com.esc.bluespring.common.utils.time.TimeMapper;
import com.esc.bluespring.domain.meeting.classes.MeetingDto.Create;
import com.esc.bluespring.domain.meeting.classes.MeetingDto.MainPageListElement;
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

    MainPageListElement toMainPageListElement(Meeting meeting, Long count);

    @Mapping(target = "fromTeam", expression = "java(teamMapper.toEntity(dto,owner))")
    Meeting toEntity(Create dto, Student owner);


}

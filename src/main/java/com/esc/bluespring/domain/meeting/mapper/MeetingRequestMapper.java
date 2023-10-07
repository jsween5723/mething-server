package com.esc.bluespring.domain.meeting.mapper;

import com.esc.bluespring.common.utils.time.TimeMapper;
import com.esc.bluespring.domain.meeting.classes.MeetingRequestDto.Detail;
import com.esc.bluespring.domain.meeting.entity.MeetingRequest;
import com.esc.bluespring.domain.member.entity.Student;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {TeamMapper.class, MeetingMapper.class, TimeMapper.class})
public interface MeetingRequestMapper {
  MeetingRequestMapper INSTANCE = Mappers.getMapper(MeetingRequestMapper.class);
  MeetingMapper meetingMapper = Mappers.getMapper(MeetingMapper.class);
  @Mapping(target = "targetMeeting", expression = "java(meetingMapper.toMainPageListElement(request.getTargetMeeting(), student))")
  @Mapping(target = "createdAt", source = "request.createdAt")
  @Mapping(target = "id", source = "request.id")
  Detail toDetail(MeetingRequest request, Student student);
  @Mapping(target = "targetMeeting", ignore = true)
  Detail toDetail(MeetingRequest request);
}

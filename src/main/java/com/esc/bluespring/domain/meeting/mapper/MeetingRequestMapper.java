package com.esc.bluespring.domain.meeting.mapper;

import com.esc.bluespring.common.utils.time.TimeMapper;
import com.esc.bluespring.domain.meeting.classes.MeetingRequestDto.Detail;
import com.esc.bluespring.domain.meeting.entity.MeetingRequest;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {TeamMapper.class, MeetingMapper.class, TimeMapper.class})
public interface MeetingRequestMapper {
  MeetingRequestMapper INSTANCE = Mappers.getMapper(MeetingRequestMapper.class);
  Detail toDetail(MeetingRequest request);
}

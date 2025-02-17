package com.esc.bluespring.domain.meeting.mapper;

import com.esc.bluespring.common.LazyLoadingAwareMapper;
import com.esc.bluespring.common.utils.time.TimeMapper;
import com.esc.bluespring.domain.meeting.classes.MeetingDto.Create;
import com.esc.bluespring.domain.meeting.classes.MeetingDto.Detail;
import com.esc.bluespring.domain.meeting.classes.MeetingDto.ListElement;
import com.esc.bluespring.domain.meeting.classes.MeetingDto.MainPageListElement;
import com.esc.bluespring.domain.meeting.classes.MeetingDto.MyMeetingPageListElement;
import com.esc.bluespring.domain.meeting.classes.MeetingDto.Request;
import com.esc.bluespring.domain.meeting.entity.Meeting;
import com.esc.bluespring.domain.meeting.entity.MeetingRequest;
import com.esc.bluespring.domain.meeting.entity.MeetingWatchlistItem;
import com.esc.bluespring.domain.member.entity.Student;
import java.util.Collection;
import org.mapstruct.Condition;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {TimeMapper.class, TeamMapper.class, MeetingRequestMapper.class})
public interface MeetingMapper extends LazyLoadingAwareMapper {

  MeetingMapper INSTANCE = Mappers.getMapper(MeetingMapper.class);
  TeamMapper teamMapper = Mappers.getMapper(TeamMapper.class);
  MeetingRequestMapper requestMapper = Mappers.getMapper(MeetingRequestMapper.class);

  @Mapping(target = "likeCount", expression = "java(meeting.getLikeCount())")
  @Mapping(target = "isLiked", expression = "java(toIsLiked(meeting, member))")
  @Mapping(target = "id", source = "meeting.id")
  @Mapping(target = "createdAt", source = "meeting.createdAt")
  @Mapping(target = "ownerTeam", source = "meeting.ownerTeam")
  @Mapping(target = "introduce", source = "meeting.introduce")
  MainPageListElement toMainPageListElement(Meeting meeting, Student member);

  @Mapping(target = "likeCount", expression = "java(meeting.getLikeCount())")
  @Mapping(target = "isLiked", expression = "java(toIsLiked(meeting, member))")
  @Mapping(target = "id", source = "meeting.id")
  @Mapping(target = "createdAt", source = "meeting.createdAt")
  @Mapping(target = "requestCount", source = "meeting")
  @Mapping(target = "introduce", source = "meeting.introduce")
  Detail toDetail(Meeting meeting, Student member);

  @Mapping(target = "myTeam", source = "meeting.ownerTeam")
  @Mapping(target = "requestCount", source = "meeting")
  MyMeetingPageListElement toMyMeetingPageListElement(Meeting meeting);

  ListElement toListElement(Meeting meeting);
  @Mapping(target = "ownerTeam", expression = "java(teamMapper.toEntity(dto,owner))")
  @Mapping(target = "watchlist", ignore = true)
  @Mapping(target = "introduce", source = "dto.introduce")
  Meeting toEntity(Create dto, Student owner);

  @Mapping(target = "targetMeeting", ignore = true)
  @Mapping(target = "status", ignore = true)
  @Mapping(target = "requesterTeam", expression = "java(teamMapper.toEntity(dto,owner))")
  @Mapping(target = "id", ignore = true)
  MeetingRequest toRequestEntity(Request dto, Student owner);

  @Mapping(target = "owner", source = "owner")
  @Mapping(target = "meeting", ignore = true)
  @Mapping(target = "id", ignore = true)
  MeetingWatchlistItem toWatchlistItemEntity(Student owner);

  default Integer getRequestCount(Meeting meeting) {
    return meeting.getJoinRequests().size();
  }

  default boolean toIsLiked(Meeting meeting, Student student) {
    return meeting.isMembersWatchlist(student);
  }

  @Condition
  default boolean isNotLazyLoadedWatchlist(
      Collection<MeetingWatchlistItem> sourceCollection) {
    return isNotLazyLoaded(sourceCollection);
  }
  @Condition
  default boolean isNotLazyLoadedRequests(
      Collection<MeetingRequest> sourceCollection) {
    return isNotLazyLoaded(sourceCollection);
  }
}

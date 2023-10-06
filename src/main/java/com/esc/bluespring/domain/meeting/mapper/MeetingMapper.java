package com.esc.bluespring.domain.meeting.mapper;

import com.esc.bluespring.common.entity.OwnerEntity;
import com.esc.bluespring.common.utils.time.TimeMapper;
import com.esc.bluespring.domain.meeting.classes.MeetingDto.Detail;
import com.esc.bluespring.domain.meeting.classes.MeetingDto.Create;
import com.esc.bluespring.domain.meeting.classes.MeetingDto.MainPageListElement;
import com.esc.bluespring.domain.meeting.classes.MeetingDto.MyMeetingPageListElement;
import com.esc.bluespring.domain.meeting.classes.MeetingDto.Request;
import com.esc.bluespring.domain.meeting.entity.Meeting;
import com.esc.bluespring.domain.meeting.entity.MeetingRequest;
import com.esc.bluespring.domain.meeting.entity.MeetingWatchlistItem;
import com.esc.bluespring.domain.member.entity.Member;
import com.esc.bluespring.domain.member.entity.Student;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {TimeMapper.class, TeamMapper.class})
public interface MeetingMapper {

    MeetingMapper INSTANCE = Mappers.getMapper(MeetingMapper.class);
    TeamMapper teamMapper = Mappers.getMapper(TeamMapper.class);

    @Mapping(target = "likeCount", expression = "java(meeting.getWatchlist().size())")
    @Mapping(target = "isLiked", expression = "java(toIsLiked(meeting, member))")
    @Mapping(target = "id", source = "meeting.id")
    @Mapping(target = "createdAt", source = "meeting.createdAt")
    @Mapping(target = "ownerTeam", source = "meeting.ownerTeam")
    MainPageListElement toMainPageListElement(Meeting meeting, Student member);
    @Mapping(target = "likeCount", expression = "java(meeting.getWatchlist().size())")
    @Mapping(target = "isLiked", expression = "java(toIsLiked(meeting, member))")
    @Mapping(target = "id", source = "meeting.id")
    @Mapping(target = "createdAt", source = "meeting.createdAt")
    Detail toDetail(Meeting meeting, Member member);

    @Mapping(target = "myTeam", source = "meeting.ownerTeam")
    @Mapping(target = "requestCount", source = "meeting")
    MyMeetingPageListElement toMyMeetingPageListElement(Meeting meeting);

    @Mapping(target = "ownerTeam", expression = "java(teamMapper.toEntity(dto,owner))")
    @Mapping(target = "watchlist", ignore = true)
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

    default boolean toIsLiked(Meeting meeting, Member member) {
        return meeting.getWatchlist().stream().map(OwnerEntity::getOwner).toList()
            .contains(member);
    }
}

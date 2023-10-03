package com.esc.bluespring.domain.meeting.mapper;

import com.esc.bluespring.domain.meeting.team.classes.TeamParticipantDto.MainPageListElement;
import com.esc.bluespring.domain.meeting.entity.TeamParticipant;
import com.esc.bluespring.domain.member.classes.MemberMapper;
import com.esc.bluespring.domain.member.entity.Student;
import java.util.UUID;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {MemberMapper.class})
public interface TeamParticipantMapper {
    TeamParticipantMapper INSTANCE = Mappers.getMapper(TeamParticipantMapper.class);
    default MainPageListElement toMainPageListElement(TeamParticipant participant) {
        return toMainPageListElement(participant.getMember(), false);
    }

    @Mapping(target = "profileImageUrl", source = "member.profileImage.url")
    MainPageListElement toMainPageListElement(Student member, boolean isOwner);

    @Mapping(target = "member", source = "id")
    TeamParticipant toEntityFromMemberId(UUID id);
}

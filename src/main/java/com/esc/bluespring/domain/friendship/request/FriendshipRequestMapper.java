package com.esc.bluespring.domain.friendship.request;

import com.esc.bluespring.domain.friendship.request.classes.FriendshipRequestDto.ListElement;
import com.esc.bluespring.domain.friendship.request.entity.FriendshipRequest;
import com.esc.bluespring.domain.member.classes.MemberMapper;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {MemberMapper.class})
public interface FriendshipRequestMapper {
    FriendshipRequestMapper INSTANCE = Mappers.getMapper(FriendshipRequestMapper.class);
    ListElement toListElement(FriendshipRequest request);
}

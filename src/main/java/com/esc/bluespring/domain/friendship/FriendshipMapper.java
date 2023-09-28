package com.esc.bluespring.domain.friendship;

import com.esc.bluespring.common.utils.time.TimeMapper;
import com.esc.bluespring.domain.friendship.classes.FriendshipDto.ListElement;
import com.esc.bluespring.domain.friendship.entity.Friendship;
import com.esc.bluespring.domain.member.classes.MemberMapper;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {MemberMapper.class, TimeMapper.class})
public interface FriendshipMapper {
    FriendshipMapper INSTANCE = Mappers.getMapper(FriendshipMapper.class);
    ListElement toListElement(Friendship friendship);
}

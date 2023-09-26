package com.esc.bluespring.domain.friendship;

import com.esc.bluespring.common.utils.time.TimeMapper;
import com.esc.bluespring.domain.friendship.classes.FriendshipDto.ListElement;
import com.esc.bluespring.domain.friendship.entity.Friendship;
import com.esc.bluespring.domain.member.classes.StudentMapper;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {StudentMapper.class, TimeMapper.class})
public interface FriendshipMapper {
    FriendshipMapper INSTANCE = Mappers.getMapper(FriendshipMapper.class);
    ListElement toListElement(Friendship friendship);
}

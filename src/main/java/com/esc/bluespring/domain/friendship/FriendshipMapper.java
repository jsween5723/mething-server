package com.esc.bluespring.domain.friendship;

import com.esc.bluespring.common.utils.time.TimeMapper;
import com.esc.bluespring.domain.friendship.classes.FriendshipDto.ListElement;
import com.esc.bluespring.domain.friendship.entity.Friendship;
import com.esc.bluespring.domain.member.classes.StudentMapper;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants.ComponentModel;

@Mapper(uses = {StudentMapper.class,
    TimeMapper.class}, injectionStrategy = InjectionStrategy.CONSTRUCTOR, componentModel = ComponentModel.SPRING)
public interface FriendshipMapper {

    ListElement toListElement(Friendship friendship);
}

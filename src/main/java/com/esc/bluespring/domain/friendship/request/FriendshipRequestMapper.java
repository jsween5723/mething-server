package com.esc.bluespring.domain.friendship.request;

import com.esc.bluespring.domain.friendship.request.classes.FriendshipRequestDto.ListElement;
import com.esc.bluespring.domain.friendship.request.entity.FriendshipRequest;
import com.esc.bluespring.domain.member.classes.StudentMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FriendshipRequestMapper {
    private final StudentMapper studentMapper;

    public ListElement toListElement(FriendshipRequest request) {
        return ListElement.builder()
            .id(request.getId())
            .message(request.getMessage())
            .requester(studentMapper.toDetail(request.getRequester()))
            .build();
    }
}

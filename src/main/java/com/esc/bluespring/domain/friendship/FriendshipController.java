package com.esc.bluespring.domain.friendship;

import com.esc.bluespring.common.CustomSlice;
import com.esc.bluespring.domain.friendship.classes.FriendshipDto.ListElement;
import com.esc.bluespring.domain.friendship.classes.FriendshipDto.SearchCondition;
import com.esc.bluespring.domain.friendship.entity.Friendship;
import com.esc.bluespring.domain.member.entity.Student;
import io.swagger.v3.oas.annotations.Operation;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/friendship")
public class FriendshipController {
    private final FriendshipService friendshipService;
    private final FriendshipMapper friendshipMapper = FriendshipMapper.INSTANCE;
    @GetMapping("/me")
    @Operation(description = "내 친구 목록 검색")
    public CustomSlice<ListElement> searchMyFriend(@ParameterObject SearchCondition condition, Student user, @ParameterObject Pageable pageable) {
        Slice<Friendship> result = friendshipService.search(condition, pageable, user);
        return new CustomSlice<>(result.map(friendshipMapper::toListElement));
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(description = "친구 삭제")
    public void deleteFriendship(@PathVariable UUID id) {
        Friendship friendship = friendshipService.find(id);
        friendshipService.deleteFriendship(friendship);
    }
}

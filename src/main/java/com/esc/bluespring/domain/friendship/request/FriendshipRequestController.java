package com.esc.bluespring.domain.friendship.request;

import com.esc.bluespring.common.CustomSlice;
import com.esc.bluespring.domain.friendship.request.classes.FriendshipRequestDto.ListElement;
import com.esc.bluespring.domain.friendship.request.classes.FriendshipRequestDto.SearchCondition;
import com.esc.bluespring.domain.friendship.request.entity.FriendshipRequest;
import com.esc.bluespring.domain.member.entity.Member;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/friendship-requests")
@RequiredArgsConstructor
@Tag(name = "친구요청 컨트롤러")
public class FriendshipRequestController {

    private final FriendshipRequestMapper mapper;
    private final FriendshipRequestService service;

    @GetMapping("/me")
    public CustomSlice<ListElement> search(Member member, @ParameterObject SearchCondition condition,
        Pageable pageable) {
        condition = new SearchCondition(condition.universityName(), condition.requesterNickname(),
            member.getId());
        Slice<FriendshipRequest> result = service.search(condition, pageable);
        return new CustomSlice<>(result.map(mapper::toListElement));
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(description = "신청자가 해당 친구 신청을 철회합니다.")
    public void remove(Member member, @PathVariable Long id) {
        FriendshipRequest request = service.find(id);
        service.delete(request, member);
    }

    @PatchMapping("{id}/accept")
    @Operation(description = "수신자가 해당 친구 신청을 수락합니다.")
    public void accept(Member member, @PathVariable Long id) {
        FriendshipRequest request = service.find(id);
        service.accept(request, member);
    }

    @PatchMapping("{id}/reject")
    @Operation(description = "수신자가 해당 친구 신청을 거절합니다.")
    public void reject(Member member, @PathVariable Long id) {
        FriendshipRequest request = service.find(id);
        service.reject(request, member);
    }
}

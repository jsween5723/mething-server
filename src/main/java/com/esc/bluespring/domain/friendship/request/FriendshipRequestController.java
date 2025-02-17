package com.esc.bluespring.domain.friendship.request;

import com.esc.bluespring.common.BaseResponse;
import com.esc.bluespring.common.CustomSlice;
import com.esc.bluespring.domain.friendship.request.classes.FriendshipRequestDto.ListElement;
import com.esc.bluespring.domain.friendship.request.classes.FriendshipRequestDto.SearchCondition;
import com.esc.bluespring.domain.friendship.request.entity.FriendshipRequest;
import com.esc.bluespring.domain.member.entity.Student;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
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

    private final FriendshipRequestMapper mapper = FriendshipRequestMapper.INSTANCE;
    private final FriendshipRequestService service;

    @GetMapping("/me")
    @Operation(description = "내 친구 목록 검색", parameters = @Parameter(required = true, in = ParameterIn.HEADER, name = "Authorization"))
    public BaseResponse<CustomSlice<ListElement>> search(Student member,
                                                         @ParameterObject SearchCondition condition,
                                                         @ParameterObject Pageable pageable) {
        condition = new SearchCondition(condition.universityName(), condition.requesterNickname(),
            member.getId());
        Slice<FriendshipRequest> result = service.search(condition, pageable);
        return new BaseResponse<>(new CustomSlice<>(result.map(mapper::toListElement)));
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(description = "신청자가 해당 친구 신청을 철회합니다.", parameters = @Parameter(required = true, in = ParameterIn.HEADER, name = "Authorization"))
    public BaseResponse<Boolean> remove(Student member, @PathVariable Long id) {
        FriendshipRequest request = service.find(id);
        service.delete(request, member);
        return new BaseResponse<>(true);
    }

    @PatchMapping("{id}/accept")
    @Operation(description = "수신자가 해당 친구 신청을 수락합니다.", parameters = @Parameter(required = true, in = ParameterIn.HEADER, name = "Authorization"))
    public BaseResponse<Boolean> accept(Student member, @PathVariable Long id) {
        FriendshipRequest request = service.find(id);
        service.accept(request, member);
        return new BaseResponse<>(true);
    }

    @PatchMapping("{id}/reject")
    @Operation(description = "수신자가 해당 친구 신청을 거절합니다.", parameters = @Parameter(required = true, in = ParameterIn.HEADER, name = "Authorization"))
    public BaseResponse<Boolean> reject(Student member, @PathVariable Long id) {
        FriendshipRequest request = service.find(id);
        service.reject(request, member);
        return new BaseResponse<>(true);
    }
}

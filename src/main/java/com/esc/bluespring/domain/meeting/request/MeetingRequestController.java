package com.esc.bluespring.domain.meeting.request;

import static com.esc.bluespring.domain.member.entity.Member.ADMIN;
import static com.esc.bluespring.domain.member.entity.Member.STUDENT;

import com.esc.bluespring.common.CustomSlice;
import com.esc.bluespring.domain.meeting.classes.MeetingRequestDto.Detail;
import com.esc.bluespring.domain.meeting.classes.MeetingRequestDto.MyRequestListElement;
import com.esc.bluespring.domain.meeting.classes.MeetingRequestDto.SearchCondition;
import com.esc.bluespring.domain.meeting.entity.MeetingRequest;
import com.esc.bluespring.domain.meeting.mapper.MeetingRequestMapper;
import com.esc.bluespring.domain.member.entity.Member;
import com.esc.bluespring.domain.member.entity.Student;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.security.RolesAllowed;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
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

@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/meeting-requests")
public class MeetingRequestController {

  private final MeetingRequestService requestService;
  private final MeetingRequestMapper mapper = Mappers.getMapper(MeetingRequestMapper.class);
  @GetMapping("me")
  @RolesAllowed({STUDENT})
  @Operation(description = "내가 포함된 신청 목록 조회")
  public CustomSlice<MyRequestListElement> searchMyRequests(SearchCondition condition, Pageable pageable, Student user) {
    Slice<MeetingRequest> result = requestService.searchMyRequests(condition, pageable,
        user);
    return new CustomSlice<>(result.map(mapper::toMyRequestListElement));
  }
  @DeleteMapping("{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @Operation(description = "내가 보낸 신청에서 특정 과팅 신청 취소 (관리자, 혹은 신청자만 사용가능)")
  public void cancel(@PathVariable UUID id, Member member) {
    MeetingRequest request = requestService.find(id);
    request.validRequesterOwner(member);
    requestService.delete(request);
  }

  @GetMapping("{id}")
  @RolesAllowed({ADMIN, STUDENT})
  @Operation(description = "특정 과팅 참여 신청 상세보기")
  public Detail getDetail(@PathVariable UUID id, Member member) {
    MeetingRequest request = requestService.find(id);
    request.validPermission(member);
    return mapper.toDetail(request, member instanceof Student student ? student : null);
  }

  @PatchMapping("{id}/accept")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @Operation(description = "우리팀에 온 신청목록에서 -> 특정 과팅 참여 신청 수락(신청 수신자, 관리자만 사용가능)")
  public void accept(@PathVariable UUID id, Member member) {
    requestService.accept(id, member);
  }

  @PatchMapping("{id}/reject")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @Operation(description = "우리팀에 온 신청목록에서 -> 특정 과팅 참여 신청 거절(신청 수신자, 관리자만 사용가능)")
  public void reject(@PathVariable UUID id, Member member) {
    requestService.reject(id, member);
  }
}

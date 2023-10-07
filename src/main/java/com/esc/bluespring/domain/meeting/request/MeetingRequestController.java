package com.esc.bluespring.domain.meeting.request;

import static com.esc.bluespring.domain.member.entity.Member.ADMIN;
import static com.esc.bluespring.domain.member.entity.Member.STUDENT;

import com.esc.bluespring.domain.meeting.classes.MeetingRequestDto.Detail;
import com.esc.bluespring.domain.meeting.entity.MeetingRequest;
import com.esc.bluespring.domain.meeting.mapper.MeetingRequestMapper;
import com.esc.bluespring.domain.member.entity.Member;
import com.esc.bluespring.domain.member.entity.Student;
import jakarta.annotation.security.RolesAllowed;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
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

  @DeleteMapping("{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void cancel(@PathVariable UUID id, Member member) {
    MeetingRequest request = requestService.find(id);
    request.validRequesterOwner(member);
    requestService.delete(request);
  }

  @GetMapping("{id}")
  @RolesAllowed({ADMIN, STUDENT})
  public Detail getDetail(@PathVariable UUID id, Member member) {
    MeetingRequest request = requestService.find(id);
    request.validPermission(member);
    return mapper.toDetail(request, member instanceof Student student ? student : null);
  }

  @PatchMapping("{id}/accept")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void accept(@PathVariable UUID id, Member member) {
    requestService.accept(id, member);
  }

  @PatchMapping("{id}/reject")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void reject(@PathVariable UUID id, Member member) {
    requestService.reject(id, member);
  }
}

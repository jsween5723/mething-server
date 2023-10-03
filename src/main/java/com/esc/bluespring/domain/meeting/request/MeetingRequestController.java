package com.esc.bluespring.domain.meeting.request;

import com.esc.bluespring.domain.meeting.entity.MeetingRequest;
import com.esc.bluespring.domain.member.entity.Member;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
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
    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void cancel(@PathVariable UUID id, Member member) {
        MeetingRequest request = requestService.find(id);
        request.validRequesterOwner(member);
        requestService.delete(request);
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

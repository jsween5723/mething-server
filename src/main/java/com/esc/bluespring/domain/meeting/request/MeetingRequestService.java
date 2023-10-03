package com.esc.bluespring.domain.meeting.request;

import com.esc.bluespring.domain.meeting.entity.MeetingRequest;
import com.esc.bluespring.domain.meeting.request.exception.MeetingRequestException.MeetingRequestNotFoundRequestException;
import com.esc.bluespring.domain.member.entity.Member;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MeetingRequestService {
    private final MeetingRequestRepository repository;

    @Transactional(readOnly = true)
    public MeetingRequest find(UUID id) {
        return repository.findById(id).orElseThrow(MeetingRequestNotFoundRequestException::new);
    }

    @Transactional
    public void delete(MeetingRequest request) {
        repository.delete(request);
    }

    @Transactional
    public void accept(UUID requestId, Member member) {
        MeetingRequest request = find(requestId);
        request.validTargetOwner(member);
        repository.rejectRemainRequestsOfMeeting(request.getTargetMeeting());
        request.accept();
    }

    @Transactional
    public void reject(UUID requestId, Member member) {
        MeetingRequest request = find(requestId);
        request.validTargetOwner(member);
        request.reject();
    }
}

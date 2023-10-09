package com.esc.bluespring.domain.meeting.request;

import com.esc.bluespring.domain.meeting.classes.MeetingRequestDto.SearchCondition;
import com.esc.bluespring.domain.meeting.entity.MeetingRequest;
import com.esc.bluespring.domain.meeting.repository.MeetingRequestQDR;
import com.esc.bluespring.domain.member.entity.Member;
import com.esc.bluespring.domain.member.entity.Student;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MeetingRequestService {
    private final MeetingRequestRepository repository;
    private final MeetingRequestQDR requestQDR;

    @Transactional(readOnly = true)
    public MeetingRequest find(UUID id) {
        return requestQDR.find(id);
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

    public Slice<MeetingRequest> searchMyRequests(SearchCondition condition, Pageable pageable, Student user) {
        return requestQDR.searchMyRequests(condition, pageable, user);
    }
}

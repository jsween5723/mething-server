package com.esc.bluespring.domain.meeting.request;

import com.esc.bluespring.domain.meeting.entity.Meeting;
import com.esc.bluespring.domain.meeting.request.entity.MeetingRequest;
import com.esc.bluespring.domain.meeting.request.exception.MeetingRequestException.MeetingRequestNotFoundRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MeetingRequestService {
    private final MeetingRequestRepository repository;
    @Transactional
    public MeetingRequest save(MeetingRequest request) {
        return repository.save(request);
    }

    @Transactional(readOnly = true)
    public MeetingRequest find(Long id) {
        return repository.findById(id).orElseThrow(MeetingRequestNotFoundRequestException::new);
    }

    @Transactional
    public void delete(MeetingRequest request) {
        repository.delete(request);
    }

    @Transactional
    public Meeting accept(MeetingRequest request) {
        repository.rejectRemainRequestsOfMeeting(request.getTargetMeeting());
        return request.accept();
    }

    @Transactional
    public void reject(MeetingRequest request) {
        request.reject();
    }
}

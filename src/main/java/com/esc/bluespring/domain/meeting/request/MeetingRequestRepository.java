package com.esc.bluespring.domain.meeting.request;

import com.esc.bluespring.domain.meeting.entity.Meeting;
import com.esc.bluespring.domain.meeting.entity.MeetingRequest;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface MeetingRequestRepository extends JpaRepository<MeetingRequest, UUID> {

    @Modifying(clearAutomatically = true)
    @Query("select r from MeetingRequest r where r.targetMeeting = :meeting")
    void rejectRemainRequestsOfMeeting(Meeting meeting);
}

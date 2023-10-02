package com.esc.bluespring.domain.meeting.team.entity;

import com.esc.bluespring.domain.meeting.entity.Meeting;
import com.esc.bluespring.domain.meeting.request.entity.MeetingRequest;
import com.esc.bluespring.domain.member.entity.Student;
import com.esc.bluespring.domain.university.entity.University;
import jakarta.persistence.Entity;
import java.util.List;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MeetingRequesterTeam extends Team {
    @Builder
    MeetingRequesterTeam(UUID id, String title, University representedUniversity, Student owner,
        List<TeamParticipant> participants) {
        super(id, title, representedUniversity, owner, participants);
    }

    public void requestTo(Meeting targetMeeting, String message) {
        MeetingRequest request = MeetingRequest.builder()
            .message(message)
            .requesterTeam(this)
            .targetMeeting(targetMeeting)
            .build();
        targetMeeting.addRequest(request);
    }
}

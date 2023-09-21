package com.esc.bluespring.domain.meeting.team.entity;

import com.esc.bluespring.domain.meeting.entity.Meeting;
import com.esc.bluespring.domain.meeting.request.entity.MeetingRequest;
import com.esc.bluespring.domain.member.entity.Student;
import com.esc.bluespring.domain.university.entity.University;
import jakarta.persistence.Entity;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MeetingRequesterTeam extends Team {
    private String message;
    @Builder
    MeetingRequesterTeam(Long id, String title, University representedUniversity, Student owner,
        List<TeamParticipant> participants, String message) {
        super(id, title, representedUniversity, owner, participants);
        this.message = message;
    }

    public void requestTo(Meeting targetMeeting) {
        MeetingRequest request = MeetingRequest.builder().targetMeeting(targetMeeting)
            .requesterTeam(this)
            .build();
        targetMeeting.addRequest(request);
    }
}

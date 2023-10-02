package com.esc.bluespring.domain.meeting.team.entity;

import com.esc.bluespring.domain.member.entity.Student;
import com.esc.bluespring.domain.university.entity.University;
import jakarta.persistence.Entity;
import java.util.List;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MeetingOwnerTeam extends Team {

    public MeetingOwnerTeam(UUID id, Student owner,
        String title, Integer maxParticipantNumber,
        University representedUniversity,
        List<TeamParticipant> participants) {
        super(id, owner, title, maxParticipantNumber, representedUniversity, participants);
    }
}

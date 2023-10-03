package com.esc.bluespring.domain.meeting.team.entity;

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
    public MeetingRequesterTeam(UUID id, Student owner, String title,
        University representedUniversity, List<TeamParticipant> participants) {
        super(id, owner, title, representedUniversity, participants);
    }
}

package com.esc.bluespring.domain.meeting.entity;

import com.esc.bluespring.domain.member.entity.Student;
import com.esc.bluespring.domain.university.entity.University;
import jakarta.persistence.Entity;
import jakarta.persistence.Transient;
import java.util.Set;
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
        University representedUniversity, Set<TeamParticipant> participants) {
        super(id, owner, title, representedUniversity, participants);
    }

    @Override
    @Transient
    public boolean isOwner() {
        return false;
    }
}

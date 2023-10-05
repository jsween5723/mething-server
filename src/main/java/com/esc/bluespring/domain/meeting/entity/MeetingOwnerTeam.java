package com.esc.bluespring.domain.meeting.entity;

import com.esc.bluespring.domain.member.entity.Student;
import com.esc.bluespring.domain.university.entity.University;
import jakarta.persistence.Entity;
import jakarta.persistence.Transient;
import java.util.List;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MeetingOwnerTeam extends Team {

    public MeetingOwnerTeam(UUID id, Student owner, String title, University representedUniversity,
        List<TeamParticipant> participants) {
        super(id, owner, title, representedUniversity, participants);
    }

    @Override
    @Transient
    public boolean isOwner() {
        return true;
    }
}

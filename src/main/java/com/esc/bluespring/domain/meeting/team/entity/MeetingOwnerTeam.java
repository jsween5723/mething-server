package com.esc.bluespring.domain.meeting.team.entity;

import com.esc.bluespring.domain.member.entity.Student;
import com.esc.bluespring.domain.university.entity.University;
import jakarta.persistence.Entity;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MeetingOwnerTeam extends Team {

    private String introduce;

    public MeetingOwnerTeam(Long id, String title, String introduce, University representedUniversity, Student owner,
        List<TeamParticipant> participants) {
        super(id, title, representedUniversity, owner, participants);
        this.introduce = introduce;
    }


}

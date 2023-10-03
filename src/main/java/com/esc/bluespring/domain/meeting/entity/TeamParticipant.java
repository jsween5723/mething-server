package com.esc.bluespring.domain.meeting.entity;

import com.esc.bluespring.common.entity.BaseEntity;
import com.esc.bluespring.domain.member.entity.Student;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "team_participants")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TeamParticipant extends BaseEntity {
    @JoinColumn(name = "team_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Team team;
    @JoinColumn(name = "participant_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Student member;

    @Builder
    TeamParticipant(UUID id, Team team, Student member) {
        super(id);
        this.team = team;
        this.member = member;
    }

    public void changeTeam(Team source) {
        team = source;
    }
}

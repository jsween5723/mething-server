package com.esc.bluespring.domain.meeting.entity;

import com.esc.bluespring.common.entity.BaseEntity;
import com.esc.bluespring.domain.meeting.team.entity.Team;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "meeting_relations")
public class MeetingRelation extends BaseEntity {
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "from_team_id", nullable = false)
    private Team myTeam;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_team_id")
    private Team otherTeam;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "meeting_id")
    private Meeting meeting;
    @Builder(access = AccessLevel.PACKAGE)
    MeetingRelation(UUID id, Team myTeam, Team otherTeam, Meeting meeting) {
        super(id);
        this.myTeam = myTeam;
        this.otherTeam = otherTeam;
        this.meeting = meeting;
    }
    MeetingRelation generateOppnentRelation() {
        return MeetingRelation.builder()
            .myTeam(otherTeam).otherTeam(myTeam).build();
    }
}

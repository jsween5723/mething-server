package com.esc.bluespring.domain.meeting.entity;

import com.esc.bluespring.common.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
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
@Table(name = "team_relations")
public class TeamRelation extends BaseEntity {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "from_team_id", nullable = false)
    private Team myTeam;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_team_id", nullable = false)
    private Team otherTeam;

    @Builder(access = AccessLevel.PACKAGE)
    TeamRelation(UUID id, Team myTeam, Team otherTeam) {
        super(id);
        this.myTeam = myTeam;
        this.otherTeam = otherTeam;
    }

    TeamRelation getOpponentRelation() {
        return TeamRelation.builder()
            .myTeam(otherTeam)
            .otherTeam(myTeam)
            .build();
    }
}

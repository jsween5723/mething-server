package com.esc.bluespring.domain.meeting.entity;

import com.esc.bluespring.common.entity.OwnerEntity;
import com.esc.bluespring.domain.member.entity.Student;
import com.esc.bluespring.domain.university.entity.University;
import jakarta.persistence.CascadeType;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "teams")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn
public abstract class Team extends OwnerEntity {

    private String title;
    private Integer maxParticipantNumber;
    @JoinColumn(name = "represented_university_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private University representedUniversity;
    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TeamParticipant> participants = new ArrayList<>();
    @OneToOne(mappedBy = "myTeam", cascade = CascadeType.ALL, orphanRemoval = true)
    private TeamRelation relation;

    Team(UUID id, Student owner, String title, University representedUniversity,
        List<TeamParticipant> participants) {
        super(id, owner);
        this.title = title;
        this.maxParticipantNumber = participants.size() + 1;
        this.representedUniversity = representedUniversity;
        this.participants = participants;
        declareTeam();
    }

    public void mapParticipants(List<TeamParticipant> source) {
        participants = source == null ? new ArrayList<>() : source;
    }

    private void declareTeam() {
        participants.forEach(participant -> participant.changeTeam(this));
    }

    void declareRelation(Team otherTeam) {
        relation = TeamRelation.builder()
            .myTeam(this)
            .otherTeam(otherTeam)
            .build();
        otherTeam.relation = relation.getOpponentRelation();
    }
}

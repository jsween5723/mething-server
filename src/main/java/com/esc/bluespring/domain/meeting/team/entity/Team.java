package com.esc.bluespring.domain.meeting.team.entity;

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
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
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
    @JoinColumn(name = "represented_university_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private University representedUniversity;
    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TeamParticipant> participants = new ArrayList<>();

    Team(Long id, String title, University representedUniversity, Student owner,
        List<TeamParticipant> participants) {
        super(id, owner);
        this.title = title;
        this.representedUniversity = representedUniversity;
        this.participants = participants;
    }

    public void mapParticipants(List<TeamParticipant> source) {
        participants = source;
    }
}

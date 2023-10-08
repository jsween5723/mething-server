package com.esc.bluespring.domain.meeting.entity;

import com.esc.bluespring.common.entity.BaseEntity;
import com.esc.bluespring.domain.member.entity.Student;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "team_participants", uniqueConstraints = {
    @UniqueConstraint(name = "member_unique", columnNames = {"team_id",
        "participant_id"})}, indexes = {
    @Index(name = "member_unique_index", columnList = "team_id, participant_id", unique = true),
    @Index(name = "created_at_index", columnList = "createdAt")})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TeamParticipant extends BaseEntity {

  @JoinColumn(name = "team_id")
  @ManyToOne(fetch = FetchType.LAZY)
  private Team team;
  @JoinColumn(name = "participant_id")
  @ManyToOne(fetch = FetchType.LAZY)
  private Student participant;

  @Builder
  TeamParticipant(UUID id, Team team, Student participant) {
    super(id);
    this.team = team;
    this.participant = participant;
  }

  public void changeTeam(Team source) {
    team = source;
  }
}

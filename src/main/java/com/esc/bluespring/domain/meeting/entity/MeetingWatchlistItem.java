package com.esc.bluespring.domain.meeting.entity;

import com.esc.bluespring.common.entity.OwnerEntity;
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

@Entity
@Table(name = "meeting_watchlist_elements", uniqueConstraints = {
    @UniqueConstraint(name = "meeting_per_member_unique", columnNames = {"meeting_id",
        "owner_id"})}, indexes = {
    @Index(name = "meeting_per_member_index", columnList = "meeting_id, owner_id", unique = true),
    @Index(name = "created_at_index", columnList = "createdAt")})
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MeetingWatchlistItem extends OwnerEntity {

  @JoinColumn(name = "meeting_id")
  @ManyToOne(fetch = FetchType.LAZY)
  private Meeting meeting;

  @Builder
  MeetingWatchlistItem(UUID id, Student owner, Meeting meeting) {
    super(id, owner);
    this.meeting = meeting;
  }

  void declareMeeting(Meeting source) {
    meeting = source;
  }
}

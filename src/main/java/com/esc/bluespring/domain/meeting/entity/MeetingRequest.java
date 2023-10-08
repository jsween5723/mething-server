package com.esc.bluespring.domain.meeting.entity;

import com.esc.bluespring.common.entity.BaseEntity;
import com.esc.bluespring.common.enums.RequestStatus;
import com.esc.bluespring.common.exception.RequestException.NotOwnerException;
import com.esc.bluespring.domain.member.entity.Member;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Index;
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
@Table(name = "meeting_requests", indexes = {@Index(name = "status", columnList = "status"),
    @Index(name = "created_at_index", columnList = "createdAt")})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MeetingRequest extends BaseEntity {

  @JoinColumn(name = "requester_team_id", updatable = false)
  @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  private MeetingRequesterTeam requesterTeam;
  @JoinColumn(name = "target_meeting_id", updatable = false)
  @ManyToOne(fetch = FetchType.LAZY)
  private Meeting targetMeeting;
  @Enumerated(EnumType.STRING)
  private RequestStatus status;
  private String message;

  @Builder
  public MeetingRequest(UUID id, MeetingRequesterTeam requesterTeam, Meeting targetMeeting,
                        RequestStatus status, String message) {
    super(id);
    this.requesterTeam = requesterTeam;
    this.targetMeeting = targetMeeting;
    this.status = status != null ? status : RequestStatus.PENDING;
    this.message = message;
  }

  public void accept() {
    this.status = RequestStatus.ACCEPTED;
    targetMeeting.engageTeam(requesterTeam);
  }

  public void reject() {
    this.status = RequestStatus.REJECTED;
  }

  public void validRequesterOwner(Member member) {
    requesterTeam.validOwner(member);
  }

  public void validTargetOwner(Member member) {
    targetMeeting.validOwner(member);
  }

  public void validPermission(Member member) {
    if (!requesterTeam.isOwner(member) && !targetMeeting.isOwner(member)) {
      throw new NotOwnerException();
    }
  }

  void declareTargetMeeting(Meeting source) {
    targetMeeting = source;
  }
}

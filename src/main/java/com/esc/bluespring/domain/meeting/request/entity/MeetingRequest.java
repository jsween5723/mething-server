package com.esc.bluespring.domain.meeting.request.entity;

import com.esc.bluespring.common.entity.BaseEntity;
import com.esc.bluespring.common.enums.RequestStatus;
import com.esc.bluespring.domain.meeting.entity.Meeting;
import com.esc.bluespring.domain.meeting.team.entity.MeetingRequesterTeam;
import com.esc.bluespring.domain.member.entity.Member;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "meeting_requests")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MeetingRequest extends BaseEntity {
    @JoinColumn(name = "requester_team_id", insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private MeetingRequesterTeam requesterTeam;
    @JoinColumn(name = "target_meeting_id", insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Meeting targetMeeting;
    @Enumerated(EnumType.STRING)
    private RequestStatus status;

    @Builder
    MeetingRequest(Long id, MeetingRequesterTeam requesterTeam,
        Meeting targetMeeting, RequestStatus status) {
        super(id);
        this.requesterTeam = requesterTeam;
        this.targetMeeting = targetMeeting;
        this.status = status;
    }


    public Meeting accept() {
        this.status = RequestStatus.ACCEPTED;
        targetMeeting.join(requesterTeam);
        return targetMeeting.getOppnentRelation();
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
}

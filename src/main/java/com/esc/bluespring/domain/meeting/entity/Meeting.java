package com.esc.bluespring.domain.meeting.entity;

import com.esc.bluespring.common.entity.BaseEntity;
import com.esc.bluespring.domain.meeting.request.entity.MeetingRequest;
import com.esc.bluespring.domain.meeting.team.entity.MeetingOwnerTeam;
import com.esc.bluespring.domain.meeting.team.entity.MeetingRequesterTeam;
import com.esc.bluespring.domain.meeting.watchlist.entity.MeetingWatchlistItem;
import com.esc.bluespring.domain.member.entity.Member;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
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
@Table(name = "meetings")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Meeting extends BaseEntity {
    private String introduce;
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "owner_team_id", nullable = false)
    private MeetingOwnerTeam ownerTeam;
    @OneToMany(mappedBy = "targetMeeting", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MeetingRequest> joinRequests = new ArrayList<>();
    @OneToMany(mappedBy = "meeting", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MeetingWatchlistItem> watchlist = new ArrayList<>();
    @OneToMany(mappedBy = "meeting", cascade = CascadeType.ALL)
    private List<MeetingRelation> relations = new ArrayList<>();


    public Meeting(UUID id, String introduce, MeetingOwnerTeam ownerTeam) {
        super(id);
        this.introduce = introduce;
        this.ownerTeam = ownerTeam;
    }
    public void joiningRequestedBy(MeetingRequesterTeam requester, String message) {
        MeetingRequest request = MeetingRequest.builder()
            .targetMeeting(this)
            .requesterTeam(requester)
            .message(message)
            .build();
        joinRequests.add(request);
    }

    public void accept(MeetingRequest meetingRequest) {
        MeetingRelation relation = MeetingRelation.builder()
            .myTeam(ownerTeam)
            .otherTeam(meetingRequest.getRequesterTeam())
            .meeting(this)
            .build();
        relations.add(relation);
        relations.add(relation.generateOppnentRelation());
    }
    public void mapWatchlist(List<MeetingWatchlistItem> source) {
        watchlist = source == null ? new ArrayList<>() : source;
    }

    public void mapMeetingRequests(List<MeetingRequest> source) {
        joinRequests = source == null ? new ArrayList<>() : source;
    }

    public void validOwner(Member member) {
        getOwnerTeam().validOwner(member);
    }
}

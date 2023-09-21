package com.esc.bluespring.domain.meeting.entity;

import com.esc.bluespring.common.entity.BaseEntity;
import com.esc.bluespring.domain.meeting.request.entity.MeetingRequest;
import com.esc.bluespring.domain.meeting.team.entity.MeetingOwnerTeam;
import com.esc.bluespring.domain.meeting.team.entity.MeetingRequesterTeam;
import com.esc.bluespring.domain.meeting.team.entity.Team;
import com.esc.bluespring.domain.member.entity.Member;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Meeting extends BaseEntity {

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "from_team_id")
    private Team fromTeam;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_team_id")
    private Team toTeam;
    @OneToMany(mappedBy = "targetMeeting", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MeetingRequest> joinRequests = new ArrayList<>();

    @Builder
    public Meeting(Long id, Team fromTeam, Team toTeam, List<MeetingRequest> joinRequests) {
        super(id);
        this.fromTeam = fromTeam;
        this.toTeam = toTeam;
        this.joinRequests = joinRequests;
    }

    public void join(MeetingRequesterTeam team) {
        this.toTeam = team;
    }
    public Meeting getOppnentRelation() {
        return Meeting.builder().fromTeam(toTeam).toTeam(fromTeam).build();
    }

    public void addRequest(MeetingRequest request) {
        joinRequests.add(request);
    }

    private MeetingOwnerTeam getOwnerTeam() {
        Team team = fromTeam instanceof MeetingOwnerTeam ? fromTeam : toTeam;
        return (MeetingOwnerTeam) team;
    }

    public void validOwner(Member member){
        getOwnerTeam().validOwner(member);
    }
}

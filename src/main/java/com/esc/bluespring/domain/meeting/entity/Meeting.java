package com.esc.bluespring.domain.meeting.entity;

import com.esc.bluespring.common.entity.BaseEntity;
import com.esc.bluespring.domain.member.entity.Member;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Index;
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
@Table(name = "meetings", indexes = @Index(name = "created_at_index", columnList = "createdAt"))
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Meeting extends BaseEntity {

  private String introduce;
  @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
  @JoinColumn(name = "owner_team_id", nullable = false, unique = true, updatable = false)
  private MeetingOwnerTeam ownerTeam;
  @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
  @JoinColumn(name = "engaged_team_id")
  private MeetingRequesterTeam engagedTeam;
  @OneToMany(mappedBy = "targetMeeting", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<MeetingRequest> joinRequests = new ArrayList<>();
  @OneToMany(mappedBy = "meeting", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<MeetingWatchlistItem> watchlist = new ArrayList<>();

  public Meeting(UUID id, String introduce, MeetingOwnerTeam ownerTeam) {
    super(id);
    this.introduce = introduce;
    this.ownerTeam = ownerTeam;
  }

  public void addRequest(MeetingRequest request) {
    request.declareTargetMeeting(this);
    joinRequests.add(request);
  }

  public void engageTeam(MeetingRequesterTeam team) {
    engagedTeam = team;
  }

  public void addWatchListItem(MeetingWatchlistItem item) {
    item.declareMeeting(this);
    watchlist.add(item);
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

  public boolean isOwner(Member member) {
    return getOwnerTeam().isOwner(member);
  }
}

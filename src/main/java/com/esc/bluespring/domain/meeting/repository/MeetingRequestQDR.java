package com.esc.bluespring.domain.meeting.repository;

import static com.esc.bluespring.domain.meeting.entity.QMeeting.meeting;
import static com.esc.bluespring.domain.meeting.entity.QMeetingOwnerTeam.meetingOwnerTeam;
import static com.esc.bluespring.domain.meeting.entity.QMeetingRequest.meetingRequest;
import static com.esc.bluespring.domain.meeting.entity.QMeetingRequesterTeam.meetingRequesterTeam;
import static com.esc.bluespring.domain.meeting.entity.QTeamParticipant.teamParticipant;

import com.esc.bluespring.common.enums.RequestStatus;
import com.esc.bluespring.common.exception.RequestException.RequestNotFoundException;
import com.esc.bluespring.common.utils.querydsl.RepositorySlicer;
import com.esc.bluespring.domain.meeting.classes.MeetingRequestDto.SearchCondition;
import com.esc.bluespring.domain.meeting.entity.Meeting;
import com.esc.bluespring.domain.meeting.entity.MeetingRequest;
import com.esc.bluespring.domain.meeting.entity.Team;
import com.esc.bluespring.domain.member.entity.Student;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.group.GroupBy;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Hibernate;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Slf4j
public class MeetingRequestQDR {

  private final JPAQueryFactory query;
  private final TeamQDR teamQDR;
  private final TeamParticipantQDR participantQDR;
  private final MeetingWatchlistQDR watchlistQDR;

  public void mapRequestsToForCount(List<Meeting> meetings) {
    Map<Meeting, List<MeetingRequest>> requestsMap = getRequestsMap(meetings);
    meetings.forEach(meeting -> meeting.mapMeetingRequests(requestsMap.get(meeting)));
  }

  public void mapRequestsToForCount(Meeting meeting) {
    mapRequestsToForCount(List.of(meeting));
  }

  private Map<Meeting, List<MeetingRequest>> getRequestsMap(List<Meeting> meetings) {
    return query.selectFrom(meetingRequest).innerJoin(meetingRequest.targetMeeting, meeting)
        .leftJoin(meetingRequest.requesterTeam)
        .where(meeting.in(meetings), meetingRequest.status.eq(RequestStatus.PENDING))
        .transform(GroupBy.groupBy(meeting).as(GroupBy.list(meetingRequest)));
  }

  @Transactional(readOnly = true)
  public Slice<MeetingRequest> searchWithMeeting(Meeting targetMeeting, SearchCondition condition,
                                                 Pageable pageable) {
    JPAQuery<MeetingRequest> dsl = query.selectFrom(meetingRequest)
        .leftJoin(meetingRequest.requesterTeam, meetingRequesterTeam).fetchJoin();
    teamQDR.fetchJoinTeam(dsl, meetingRequesterTeam, "requester");
    List<MeetingRequest> result = dsl.where(toWhereCondition(condition),
            meetingRequest.targetMeeting.eq(targetMeeting)).offset(pageable.getOffset())
        .limit(pageable.getPageSize() + 1).fetch();
    participantQDR.mapParticipantsTo(extractTeams(result));
    return RepositorySlicer.toSlice(result, pageable);
  }

  @Transactional(readOnly = true)
  public Slice<MeetingRequest> searchMyRequests(SearchCondition condition,
                                                 Pageable pageable, Student user) {
    JPAQuery<MeetingRequest> dsl = query.selectFrom(meetingRequest)
        .leftJoin(meetingRequest.requesterTeam, meetingRequesterTeam)
        .leftJoin(meetingRequesterTeam.participants, teamParticipant)
            .leftJoin(meetingRequest.targetMeeting, meeting).fetchJoin()
            .leftJoin(meeting.ownerTeam, meetingOwnerTeam).fetchJoin();
    teamQDR.fetchJoinTeam(dsl, meetingOwnerTeam, "owner");
    List<MeetingRequest> result = dsl.where(toWhereCondition(condition),
            teamParticipant.participant.eq(user).or(meetingRequesterTeam.owner.eq(user)))
        .limit(pageable.getPageSize() + 1).fetch();
    participantQDR.mapParticipantsTo(extractTeams(result));
    return RepositorySlicer.toSlice(result, pageable);
  }

  @Transactional(readOnly = true)
  public MeetingRequest find(UUID id) {
    log.info("start");
    JPAQuery<MeetingRequest> dsl = query.selectFrom(meetingRequest)
        .leftJoin(meetingRequest.requesterTeam, meetingRequesterTeam).fetchJoin()
        .leftJoin(meetingRequest.targetMeeting, meeting).fetchJoin()
        .leftJoin(meeting.ownerTeam, meetingOwnerTeam).fetchJoin();
    teamQDR.fetchJoinTeam(dsl, meetingRequesterTeam, "requester");
    teamQDR.fetchJoinTeam(dsl, meetingOwnerTeam, "owner");
    MeetingRequest result = dsl.where(meetingRequest.id.eq(id)).fetchFirst();
    if (result == null) {
      throw new RequestNotFoundException();
    }
    participantQDR.mapParticipantsTo(extractTeams(result));
    watchlistQDR.mapWatchlistTo(result.getTargetMeeting());
    return result;
  }

  private BooleanBuilder toWhereCondition(SearchCondition condition) {
    BooleanBuilder builder = new BooleanBuilder();
    if (condition.statuses() != null) {
      builder.and(meetingRequest.status.in(condition.statuses()));
    }
    return builder;
  }

  private Set<Team> extractTeams(MeetingRequest request) {
    LinkedHashSet<Team> teams = new LinkedHashSet<>();
    if (Hibernate.isInitialized(request.getRequesterTeam())) {
      teams.add(request.getRequesterTeam());
    }
    if (Hibernate.isInitialized(request.getTargetMeeting()) && Hibernate.isInitialized(
        request.getTargetMeeting().getOwnerTeam())) {
      teams.add(request.getTargetMeeting().getOwnerTeam());
    }
    return teams;
  }

  private Set<Team> extractTeams(List<MeetingRequest> request) {
    return request.stream().map(this::extractTeams).flatMap(Collection::stream)
        .collect(Collectors.toSet());
  }
}

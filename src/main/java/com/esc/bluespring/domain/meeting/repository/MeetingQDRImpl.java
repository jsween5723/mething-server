package com.esc.bluespring.domain.meeting.repository;

import static com.esc.bluespring.domain.locationDistrict.entity.QLocationDistrict.locationDistrict;
import static com.esc.bluespring.domain.meeting.entity.QMeeting.meeting;
import static com.esc.bluespring.domain.meeting.entity.QMeetingOwnerTeam.meetingOwnerTeam;
import static com.esc.bluespring.domain.meeting.entity.QMeetingRequesterTeam.meetingRequesterTeam;
import static com.esc.bluespring.domain.meeting.entity.QTeamParticipant.teamParticipant;

import com.esc.bluespring.common.utils.querydsl.RepositorySlicer;
import com.esc.bluespring.domain.meeting.classes.MeetingDto.MainPageSearchCondition;
import com.esc.bluespring.domain.meeting.entity.Meeting;
import com.esc.bluespring.domain.meeting.entity.Team;
import com.esc.bluespring.domain.meeting.exception.MeetingException.MeetingNotFoundException;
import com.esc.bluespring.domain.member.entity.Member;
import com.esc.bluespring.domain.member.entity.Student;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
public class MeetingQDRImpl implements MeetingQDR {

  private final JPAQueryFactory query;
  private final MeetingRequestQDR requestQDR;
  private final MeetingWatchlistQDR watchlistQDR;
  private final TeamParticipantQDR participantQDR;
  private final TeamQDR teamQDR;

  @Transactional(readOnly = true)
  public Slice<Meeting> searchMainPageList(Member user, MainPageSearchCondition condition,
                                           Pageable pageable) {
    JPAQuery<Meeting> dsl = query.selectFrom(meeting).leftJoin(meeting.ownerTeam, meetingOwnerTeam)
        .fetchJoin();
    teamQDR.fetchJoinTeam(dsl, meetingOwnerTeam, "owner");
    List<Meeting> meetings = dsl.where(meeting.engagedTeam.isNull(),
            toWhereCondition(condition, user)).offset(pageable.getOffset())
        .limit(pageable.getPageSize() + 1).fetch();
    participantQDR.mapParticipantsTo(extractTeam(meetings));
    watchlistQDR.mapWatchlistTo(meetings);
    return RepositorySlicer.toSlice(meetings, pageable);
  }

  @Transactional(readOnly = true)
  public Meeting find(UUID id, boolean requireEngagedTeam) {
    JPAQuery<Meeting> dsl = query.selectFrom(meeting).leftJoin(meeting.ownerTeam, meetingOwnerTeam)
        .fetchJoin();
    if (requireEngagedTeam) {
      dsl.innerJoin(meeting.engagedTeam, meetingRequesterTeam).fetchJoin();
    } else {
      dsl.leftJoin(meeting.engagedTeam, meetingRequesterTeam).fetchJoin();
    }
    teamQDR.fetchJoinTeam(dsl, meetingOwnerTeam, "owner");
    teamQDR.fetchJoinTeam(dsl, meetingRequesterTeam, "requester");
    Meeting result = dsl.where(meeting.id.eq(id)).fetchFirst();
    if (result == null) {
      throw new MeetingNotFoundException();
    }
    participantQDR.mapParticipantsTo(extractTeam(result));
    watchlistQDR.mapWatchlistTo(result);
    requestQDR.mapRequestsToForCount(result);
    return result;
  }

  @Transactional(readOnly = true)
  public Meeting find(UUID id) {
    return find(id, false);
  }

  private Set<Team> extractTeam(List<Meeting> meetings) {
    Set<Team> teams = meetings.stream().map(Meeting::getOwnerTeam).map(Team.class::cast)
        .collect(Collectors.toSet());
    teams.addAll(meetings.stream().filter(meeting -> meeting.getEngagedTeam() != null)
        .map(Meeting::getEngagedTeam).map(Team.class::cast).collect(Collectors.toSet()));
    return teams;
  }

  private Set<Team> extractTeam(Meeting meeting) {
    return extractTeam(List.of(meeting));
  }


  @Override
  @Transactional(readOnly = true)
  public Slice<Meeting> searchMyMeetingList(Student user, Pageable pageable) {
    JPAQuery<Meeting> dsl = query.selectFrom(meeting).leftJoin(meeting.ownerTeam, meetingOwnerTeam)
        .fetchJoin().leftJoin(meetingOwnerTeam.participants, teamParticipant);
    teamQDR.fetchJoinTeam(dsl, meetingOwnerTeam, "owner");
    List<Meeting> meetings = dsl.where(meeting.engagedTeam.isNull(),
            meetingOwnerTeam.owner.eq(user).or(teamParticipant.participant.eq(user)))
        .offset(pageable.getOffset()).limit(pageable.getPageSize() + 1).fetch();
    participantQDR.mapParticipantsTo(extractTeam(meetings));
    requestQDR.mapRequestsToForCount(meetings);
    return RepositorySlicer.toSlice(meetings, pageable);
  }

  private BooleanBuilder toWhereCondition(MainPageSearchCondition condition, Member member) {
    BooleanBuilder builder = new BooleanBuilder();
    if (condition.isMyLocation() && member instanceof Student student) {
      builder.and(locationDistrict.id.eq(
          student.getSchoolInformation().getMajor().getUniversity().getLocationDistrict().getId()));
    }
    return builder;
  }
}

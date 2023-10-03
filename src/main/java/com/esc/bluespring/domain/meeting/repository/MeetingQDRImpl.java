package com.esc.bluespring.domain.meeting.repository;

import static com.esc.bluespring.domain.locationDistrict.entity.QLocationDistrict.locationDistrict;
import static com.esc.bluespring.domain.meeting.entity.QMeeting.meeting;
import static com.esc.bluespring.domain.meeting.entity.QMeetingOwnerTeam.meetingOwnerTeam;
import static com.esc.bluespring.domain.meeting.entity.QMeetingRequest.meetingRequest;
import static com.esc.bluespring.domain.meeting.entity.QMeetingWatchlistItem.meetingWatchlistItem;
import static com.esc.bluespring.domain.meeting.entity.QTeam.team;
import static com.esc.bluespring.domain.meeting.entity.QTeamParticipant.teamParticipant;
import static com.esc.bluespring.domain.member.entity.QStudent.student;
import static com.esc.bluespring.domain.university.entity.QUniversity.university;

import com.esc.bluespring.common.utils.querydsl.RepositorySlicer;
import com.esc.bluespring.domain.meeting.classes.MeetingDto.MainPageSearchCondition;
import com.esc.bluespring.domain.meeting.entity.Meeting;
import com.esc.bluespring.domain.meeting.entity.MeetingRequest;
import com.esc.bluespring.domain.meeting.entity.MeetingWatchlistItem;
import com.esc.bluespring.domain.meeting.entity.QMeetingOwnerTeam;
import com.esc.bluespring.domain.meeting.entity.Team;
import com.esc.bluespring.domain.meeting.entity.TeamParticipant;
import com.esc.bluespring.domain.member.entity.Member;
import com.esc.bluespring.domain.member.entity.QStudent;
import com.esc.bluespring.domain.member.entity.Student;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.group.GroupBy;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
public class MeetingQDRImpl implements MeetingQDR {

    private final JPAQueryFactory query;
    @Transactional(readOnly = true)
    public Slice<Meeting> searchMainPageList(Member user, MainPageSearchCondition condition,
        Pageable pageable) {
        List<Meeting> meetings = query.selectFrom(meeting)
            .innerJoin(meeting.ownerTeam.as(QMeetingOwnerTeam.class), meetingOwnerTeam).fetchJoin()
            .leftJoin(meetingOwnerTeam.owner.as(QStudent.class), student).fetchJoin()
            .leftJoin(meetingOwnerTeam.representedUniversity, university).fetchJoin()
            .leftJoin(university.locationDistrict, locationDistrict).fetchJoin().fetchJoin()
            .where(toWhereCondition(condition, user)).offset(pageable.getOffset())
            .limit(pageable.getPageSize() + 1).fetch();
        List<Team> teams = meetings.stream().map(Meeting::getOwnerTeam).map(team -> (Team) team)
            .toList();
        Map<Team, List<TeamParticipant>> participantsMap = getParticipantsMap(teams);
        Map<Meeting, List<MeetingWatchlistItem>> watchlistItemMap = getWatchlistItemMap(meetings);
        meetings.forEach(meeting -> meeting.mapWatchlist(watchlistItemMap.get(meeting)));
        meetings.forEach(meeting -> meeting.getOwnerTeam()
            .mapParticipants(participantsMap.get(meeting.getOwnerTeam())));
        return RepositorySlicer.toSlice(meetings, pageable);
    }

    private Map<Meeting, List<MeetingWatchlistItem>> getWatchlistItemMap(List<Meeting> meetings) {
        return query.selectFrom(meetingWatchlistItem).join(meetingWatchlistItem.meeting)
            .where(meetingWatchlistItem.meeting.in(meetings)).transform(
                GroupBy.groupBy(meetingWatchlistItem.meeting)
                    .as(GroupBy.list(meetingWatchlistItem)));
    }

    private Map<Team, List<TeamParticipant>> getParticipantsMap(List<Team> teams) {
        return query.selectFrom(teamParticipant).innerJoin(teamParticipant.team, team)
            .where(team.in(teams))
            .transform(GroupBy.groupBy(team).as(GroupBy.list(teamParticipant)));
    }

    @Override
    @Transactional(readOnly = true)
    public Slice<Meeting> searchMyMeetingList(Student user, Pageable pageable) {
        List<Meeting> meetings = query.selectFrom(meeting)
            .innerJoin(meeting.ownerTeam.as(QMeetingOwnerTeam.class), meetingOwnerTeam).fetchJoin()
            .leftJoin(meetingOwnerTeam.participants, teamParticipant)
            .leftJoin(meetingOwnerTeam.owner.as(QStudent.class), student).fetchJoin()
            .leftJoin(meetingOwnerTeam.representedUniversity, university).fetchJoin()
            .leftJoin(university.locationDistrict, locationDistrict).fetchJoin()
            .where(meetingOwnerTeam.owner.eq(user).or(teamParticipant.member.eq(user)))
            .offset(pageable.getOffset()).limit(pageable.getPageSize() + 1).fetch();
        List<Team> teams = meetings.stream().map(Meeting::getOwnerTeam).map(team -> (Team) team)
            .toList();
        Map<Team, List<TeamParticipant>> participantMap = getParticipantsMap(teams);
        teams.forEach(team -> team.mapParticipants(participantMap.get(team)));
        Map<Meeting, List<MeetingRequest>> requestMap = query.selectFrom(meetingRequest)
            .innerJoin(meetingRequest.targetMeeting, meeting).where(meeting.in(meetings))
            .transform(GroupBy.groupBy(meeting).as(GroupBy.list(meetingRequest)));
        meetings.forEach(meeting -> meeting.mapMeetingRequests(requestMap.get(meeting)));
        return RepositorySlicer.toSlice(meetings, pageable);
    }

    private BooleanBuilder toWhereCondition(MainPageSearchCondition condition, Member member) {
        BooleanBuilder builder = new BooleanBuilder();
        if (condition.isMyLocation() && member instanceof Student student) {
            builder.and(locationDistrict.id.eq(
                student.getSchoolInformation().getMajor().getUniversity().getLocationDistrict()
                    .getId()));
        }
        return builder;
    }
}

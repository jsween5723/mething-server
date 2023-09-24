package com.esc.bluespring.domain.meeting.repository;

import static com.esc.bluespring.domain.locationDistrict.entity.QLocationDistrict.locationDistrict;
import static com.esc.bluespring.domain.meeting.entity.QMeeting.meeting;
import static com.esc.bluespring.domain.meeting.team.entity.QMeetingOwnerTeam.meetingOwnerTeam;
import static com.esc.bluespring.domain.meeting.team.entity.QTeamParticipant.teamParticipant;
import static com.esc.bluespring.domain.meeting.watchlist.entity.QMeetingWatchlistItem.meetingWatchlistItem;
import static com.esc.bluespring.domain.member.entity.QStudent.student;
import static com.esc.bluespring.domain.university.entity.QUniversity.university;
import static com.querydsl.core.types.ExpressionUtils.count;

import com.esc.bluespring.common.entity.BaseEntity;
import com.esc.bluespring.common.utils.querydsl.RepositorySlicer;
import com.esc.bluespring.domain.meeting.classes.MeetingDto.MainPageListElement;
import com.esc.bluespring.domain.meeting.classes.MeetingDto.SearchCondition;
import com.esc.bluespring.domain.meeting.entity.Meeting;
import com.esc.bluespring.domain.meeting.team.entity.MeetingOwnerTeam;
import com.esc.bluespring.domain.meeting.team.entity.QMeetingOwnerTeam;
import com.esc.bluespring.domain.meeting.team.entity.QTeam;
import com.esc.bluespring.domain.meeting.team.entity.Team;
import com.esc.bluespring.domain.meeting.team.entity.TeamParticipant;
import com.esc.bluespring.domain.meeting.team.repository.MeetingOwnerTeamQDR;
import com.esc.bluespring.domain.meeting.watchlist.entity.MeetingWatchlistItem;
import com.esc.bluespring.domain.member.entity.QMember;
import com.esc.bluespring.domain.member.entity.Student;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.group.GroupBy;
import com.querydsl.core.types.ConstructorExpression;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
public class MeetingQDRImpl implements MeetingQDR {

    private final JPAQueryFactory query;
    private final MeetingOwnerTeamQDR meetingOwnerTeamQDR;

    @Transactional(readOnly = true)
    public Slice<Meeting> search(Student user, SearchCondition condition, Pageable pageable) {
        QTeam qTeam = new QTeam(meetingOwnerTeam);
        List<Meeting> meetings = query.select(meeting).from(meeting)
            .innerJoin(meeting.fromTeam.as(QMeetingOwnerTeam.class), meetingOwnerTeam).fetchJoin()
            .leftJoin(meetingOwnerTeam.owner, student._super).fetchJoin()
            .leftJoin(meetingOwnerTeam.representedUniversity, university).fetchJoin()
            .leftJoin(university.locationDistrict, locationDistrict).fetchJoin().fetchJoin()
            .where(toWhereCondition(condition, user), meeting.fromTeam.instanceOf(MeetingOwnerTeam.class)).offset(pageable.getOffset())
            .limit(pageable.getPageSize()).fetch();
        List<Long> teamIds = meetings.stream().filter(Objects::nonNull).map(BaseEntity::getId)
            .toList();
        List<Long> meetingIds = meetings.stream().map(BaseEntity::getId).toList();
        Map<Team, List<TeamParticipant>> participantsMap = query.selectFrom(teamParticipant)
            .join(teamParticipant.team, meetingOwnerTeam._super).fetchJoin()
            .where(meetingOwnerTeam._super.id.in(teamIds))
            .transform(GroupBy.groupBy(meetingOwnerTeam._super).as(GroupBy.list(teamParticipant)));
        Map<Meeting, List<MeetingWatchlistItem>> watchlistItemMap = query.selectFrom(
                meetingWatchlistItem).join(meetingWatchlistItem.meeting)
            .where(meetingWatchlistItem.meeting.id.in(meetingIds)).transform(
                GroupBy.groupBy(meetingWatchlistItem.meeting)
                    .as(GroupBy.list(meetingWatchlistItem)));
        meetings.forEach(meeting -> meeting.mapWatchlist(watchlistItemMap.get(meeting)));
        meetings.forEach(meeting -> meeting.getFromTeam()
            .mapParticipants(participantsMap.get(meeting.getFromTeam())));
        return RepositorySlicer.toSlice(meetings, pageable);
    }

    private BooleanBuilder toWhereCondition(SearchCondition condition, Student student) {
        BooleanBuilder builder = new BooleanBuilder();
//        if (condition.isMyLocation()) {
//            builder.and(locationDistrict.id.eq(
//                student.getSchoolInformation().getMajor().getUniversity().getLocationDistrict()
//                    .getId()));
//        }
        return builder;
    }

    public ConstructorExpression<MainPageListElement> toMainPageListElement(Student student,
        QMember owner) {
        return Projections.constructor(MainPageListElement.class, meeting.id,
            meetingOwnerTeamQDR.toMainPageListElement(meetingOwnerTeam), count(meeting.watchlist),
            query.select(meetingWatchlistItem.owner.eq(student)
                .and(meetingWatchlistItem.meeting.eq(meeting))).from(meetingWatchlistItem),
            meeting.createdAt.milliSecond().castToNum(Long.class).multiply(1000));
    }
}

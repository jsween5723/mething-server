package com.esc.bluespring.domain.meeting.repository;

import static com.esc.bluespring.domain.locationDistrict.entity.QLocationDistrict.locationDistrict;
import static com.esc.bluespring.domain.meeting.entity.QMeeting.meeting;
import static com.esc.bluespring.domain.meeting.request.entity.QMeetingRequest.meetingRequest;
import static com.esc.bluespring.domain.meeting.team.entity.QMeetingOwnerTeam.meetingOwnerTeam;
import static com.esc.bluespring.domain.meeting.team.entity.QTeamParticipant.teamParticipant;
import static com.esc.bluespring.domain.meeting.watchlist.entity.QMeetingWatchlistItem.meetingWatchlistItem;
import static com.esc.bluespring.domain.member.entity.QStudent.student;
import static com.esc.bluespring.domain.university.entity.QUniversity.university;
import static com.querydsl.core.types.ExpressionUtils.count;

import com.esc.bluespring.common.entity.BaseEntity;
import com.esc.bluespring.common.utils.querydsl.RepositorySlicer;
import com.esc.bluespring.domain.meeting.classes.MeetingDto.MainPageListElement;
import com.esc.bluespring.domain.meeting.classes.MeetingDto.MainPageSearchCondition;
import com.esc.bluespring.domain.meeting.entity.Meeting;
import com.esc.bluespring.domain.meeting.request.entity.MeetingRequest;
import com.esc.bluespring.domain.meeting.team.entity.MeetingOwnerTeam;
import com.esc.bluespring.domain.meeting.team.entity.QMeetingOwnerTeam;
import com.esc.bluespring.domain.meeting.team.entity.Team;
import com.esc.bluespring.domain.meeting.team.entity.TeamParticipant;
import com.esc.bluespring.domain.meeting.team.repository.MeetingOwnerTeamQDR;
import com.esc.bluespring.domain.meeting.watchlist.entity.MeetingWatchlistItem;
import com.esc.bluespring.domain.member.entity.Member;
import com.esc.bluespring.domain.member.entity.QMember;
import com.esc.bluespring.domain.member.entity.QStudent;
import com.esc.bluespring.domain.member.entity.Student;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.group.GroupBy;
import com.querydsl.core.types.ConstructorExpression;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
public class MeetingQDRImpl implements MeetingQDR {

    private final JPAQueryFactory query;
    private final MeetingOwnerTeamQDR meetingOwnerTeamQDR;

    @Transactional(readOnly = true)
    public Slice<Meeting> searchMainPageList(Member user, MainPageSearchCondition condition,
        Pageable pageable) {
        List<Meeting> meetingRelations = query.selectFrom(meeting)
            .innerJoin(meeting.ownerTeam.as(QMeetingOwnerTeam.class), meetingOwnerTeam).fetchJoin()
            .leftJoin(meetingOwnerTeam.owner.as(QStudent.class), student).fetchJoin()
            .leftJoin(meetingOwnerTeam.representedUniversity, university).fetchJoin()
            .leftJoin(university.locationDistrict, locationDistrict).fetchJoin().fetchJoin()
            .where(toWhereCondition(condition, user))
            .offset(pageable.getOffset()).limit(pageable.getPageSize() + 1).fetch();
        List<UUID> teamIds = meetingRelations.stream().map(Meeting::getOwnerTeam).map(BaseEntity::getId)
            .toList();
        List<UUID> meetingIds = meetingRelations.stream().map(BaseEntity::getId).toList();
        Map<Team, List<TeamParticipant>> participantsMap = getParticipantsMap(teamIds);
        Map<Meeting, List<MeetingWatchlistItem>> watchlistItemMap = getWatchlistItemMap(meetingIds);
        meetingRelations.forEach(meeting -> meeting.mapWatchlist(watchlistItemMap.get(meeting)));
        meetingRelations.forEach(meeting -> meeting.getOwnerTeam()
            .mapParticipants(participantsMap.get(meeting.getOwnerTeam())));
        return RepositorySlicer.toSlice(meetingRelations, pageable);
    }

    private Map<Meeting, List<MeetingWatchlistItem>> getWatchlistItemMap(List<UUID> meetingIds) {
        return query.selectFrom(meetingWatchlistItem).join(meetingWatchlistItem.meeting)
            .where(meetingWatchlistItem.meeting.id.in(meetingIds)).transform(
                GroupBy.groupBy(meetingWatchlistItem.meeting)
                    .as(GroupBy.list(meetingWatchlistItem)));
    }

    private Map<Team, List<TeamParticipant>> getParticipantsMap(List<UUID> teamIds) {
        return query.selectFrom(teamParticipant).join(teamParticipant.team, meetingOwnerTeam._super)
            .fetchJoin().where(meetingOwnerTeam._super.id.in(teamIds))
            .transform(GroupBy.groupBy(meetingOwnerTeam._super).as(GroupBy.list(teamParticipant)));
    }

    @Override
    @Transactional(readOnly = true)
    public Slice<Meeting> searchMyMeetingList(Student user, Pageable pageable) {
        List<Meeting> meetingRelations = query.selectFrom(meeting)
            .innerJoin(meeting.ownerTeam.as(QMeetingOwnerTeam.class), meetingOwnerTeam).fetchJoin()
            .leftJoin(meetingOwnerTeam.owner.as(QStudent.class), student).fetchJoin()
            .leftJoin(meetingOwnerTeam.representedUniversity, university).fetchJoin()
            .leftJoin(university.locationDistrict, locationDistrict).fetchJoin()
            .leftJoin(meetingOwnerTeam.participants, teamParticipant).fetchJoin()
            .leftJoin(teamParticipant.member)
            .where(meeting.ownerTeam.instanceOf(MeetingOwnerTeam.class),
                meetingOwnerTeam.owner.eq(user).or(teamParticipant.member.eq(user)))
            .offset(pageable.getOffset()).limit(pageable.getPageSize() + 1).fetch();
        List<UUID> meetingIds = meetingRelations.stream().map(BaseEntity::getId).toList();
        Map<Meeting, List<MeetingRequest>> requestMap = query.selectFrom(meetingRequest)
            .where(meetingRequest.targetMeeting.id.in(meetingIds)).transform(
                GroupBy.groupBy(meetingRequest.targetMeeting).as(GroupBy.list(meetingRequest)));
        meetingRelations.forEach(meeting -> meeting.mapMeetingRequests(requestMap.get(meeting)));
        return RepositorySlicer.toSlice(meetingRelations, pageable);
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

    public ConstructorExpression<MainPageListElement> toMainPageListElement(Student student,
        QMember owner) {
        return Projections.constructor(MainPageListElement.class, meeting.id,
            meetingOwnerTeamQDR.toMainPageListElement(meetingOwnerTeam), count(meeting.watchlist),
            query.select(meetingWatchlistItem.owner.eq(student)
                .and(meetingWatchlistItem.meeting.eq(meeting))).from(meetingWatchlistItem),
            meeting.createdAt.milliSecond().castToNum(Long.class).multiply(1000));
    }
}

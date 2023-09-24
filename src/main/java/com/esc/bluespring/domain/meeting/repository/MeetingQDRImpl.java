package com.esc.bluespring.domain.meeting.repository;

import static com.esc.bluespring.domain.locationDistrict.entity.QLocationDistrict.locationDistrict;
import static com.esc.bluespring.domain.meeting.entity.QMeeting.meeting;
import static com.esc.bluespring.domain.meeting.team.entity.QMeetingOwnerTeam.meetingOwnerTeam;
import static com.esc.bluespring.domain.meeting.team.entity.QTeam.team;
import static com.esc.bluespring.domain.meeting.watchlist.entity.QMeetingWatchlistItem.meetingWatchlistItem;
import static com.esc.bluespring.domain.member.entity.QMember.member;
import static com.esc.bluespring.domain.university.entity.QUniversity.university;
import static com.querydsl.core.types.ExpressionUtils.count;

import com.esc.bluespring.common.utils.querydsl.RepositorySlicer;
import com.esc.bluespring.domain.meeting.classes.MeetingDto.MainPageListElement;
import com.esc.bluespring.domain.meeting.classes.MeetingDto.SearchCondition;
import com.esc.bluespring.domain.meeting.entity.QMeeting;
import com.esc.bluespring.domain.meeting.team.repository.MeetingOwnerTeamQDR;
import com.esc.bluespring.domain.member.entity.Student;
import com.querydsl.core.types.ConstructorExpression;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

@RequiredArgsConstructor
public class MeetingQDRImpl implements MeetingQDR {

    private final JPAQueryFactory query;
    private final MeetingOwnerTeamQDR meetingOwnerTeamQDR;

    public Slice<MainPageListElement> search(Student student, SearchCondition condition,
        Pageable pageable) {

        List<MainPageListElement> fetch = query.select(
                toMainPageListElement(meeting, student))
            .innerJoin(meetingOwnerTeam).fetchJoin()
            .leftJoin(meetingOwnerTeam.representedUniversity, university).fetchJoin()
            .leftJoin(university.locationDistrict, locationDistrict).fetchJoin()
            .join(meetingOwnerTeam.owner, member).fetchJoin()
            .leftJoin(meetingOwnerTeam.participants).fetchJoin()
            .groupBy(meetingWatchlistItem)
            .having(meetingWatchlistItem.meeting.id.eq(meeting.id))
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();
        return RepositorySlicer.toSlice(fetch, pageable);
    }

    public ConstructorExpression<MainPageListElement> toMainPageListElement(QMeeting meeting,
        Student student) {
        return Projections.constructor(MainPageListElement.class, meeting.id, meetingOwnerTeamQDR.toMainPageListElement(meetingOwnerTeam),
            count(meeting.watchlist), query.select(meetingWatchlistItem.owner.eq(student)
                .and(meetingWatchlistItem.meeting.eq(meeting))).from(meetingWatchlistItem)
            , meeting.createdAt);
    }
}

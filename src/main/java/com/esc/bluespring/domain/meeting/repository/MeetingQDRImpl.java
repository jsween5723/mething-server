package com.esc.bluespring.domain.meeting.repository;

import static com.esc.bluespring.domain.locationDistrict.entity.QLocationDistrict.locationDistrict;
import static com.esc.bluespring.domain.meeting.entity.QMeeting.meeting;
import static com.esc.bluespring.domain.meeting.team.entity.QTeam.team;
import static com.esc.bluespring.domain.meeting.watchlist.entity.QMeetingWatchlistItem.meetingWatchlistItem;
import static com.esc.bluespring.domain.member.entity.QMember.member;
import static com.esc.bluespring.domain.university.entity.QUniversity.university;

import com.esc.bluespring.common.utils.querydsl.RepositorySlicer;
import com.esc.bluespring.domain.meeting.classes.MeetingDto.MainPageListElement;
import com.esc.bluespring.domain.meeting.classes.MeetingDto.SearchCondition;
import com.esc.bluespring.domain.meeting.entity.QMeeting;
import com.esc.bluespring.domain.meeting.watchlist.entity.QMeetingWatchlistItem;
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

    public Slice<MainPageListElement> search(SearchCondition condition, Pageable pageable) {

        List<MainPageListElement> fetch = query.select(
                toMainPageListElement(meeting, meetingWatchlistItem))
            .leftJoin(meeting.fromTeam, team).fetchJoin()
            .leftJoin(team.representedUniversity, university).fetchJoin()
            .leftJoin(university.locationDistrict, locationDistrict).fetchJoin()
            .join(team.owner, member).fetchJoin()
            .leftJoin(team.participants).fetchJoin()
            .groupBy(meetingWatchlistItem)
            .having(meetingWatchlistItem.meeting.id.eq(meeting.id))
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();
        return RepositorySlicer.toSlice(fetch, pageable);
    }

    public ConstructorExpression<MainPageListElement> toMainPageListElement(QMeeting meeting, QMeetingWatchlistItem watchlistItem) {
        return Projections.constructor(MainPageListElement.class, meeting.id, meeting.fromTeam, watchlistItem.count(), meeting.createdAt);
    }
}

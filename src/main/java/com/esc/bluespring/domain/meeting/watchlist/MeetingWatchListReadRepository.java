package com.esc.bluespring.domain.meeting.watchlist;

import static com.esc.bluespring.domain.meeting.entity.QMeeting.meeting;
import static com.esc.bluespring.domain.meeting.entity.QMeetingOwnerTeam.meetingOwnerTeam;
import static com.esc.bluespring.domain.meeting.entity.QMeetingWatchlistItem.meetingWatchlistItem;

import com.esc.bluespring.common.utils.querydsl.RepositorySlicer;
import com.esc.bluespring.domain.meeting.entity.Meeting;
import com.esc.bluespring.domain.meeting.entity.Team;
import com.esc.bluespring.domain.meeting.repository.MeetingWatchlistQDR;
import com.esc.bluespring.domain.meeting.repository.TeamParticipantQDR;
import com.esc.bluespring.domain.meeting.repository.TeamQDR;
import com.esc.bluespring.domain.member.entity.Student;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class MeetingWatchListReadRepository {

    private final JPAQueryFactory query;
    private final TeamQDR teamQDR;
    private final TeamParticipantQDR participantQDR;
    private final MeetingWatchlistQDR watchlistQDR;

    public Slice<Meeting> getMyList(Student student, Pageable pageable) {
        JPAQuery<Meeting> dsl = query.select(meeting).from(meeting).join(meetingWatchlistItem)
            .on(meetingWatchlistItem.meeting.eq(meeting)
                .and(meetingWatchlistItem.owner.eq(student)))
            .leftJoin(meeting.ownerTeam, meetingOwnerTeam).fetchJoin();
        teamQDR.fetchJoinTeam(dsl, meetingOwnerTeam, "owner");
        List<Meeting> meetings = dsl.where(toWhereCondition(student)).offset(pageable.getOffset())
            .limit(pageable.getPageSize() + 1).fetch();
        participantQDR.mapParticipantsTo(extractTeam(meetings));
        watchlistQDR.mapWatchlistTo(meetings);
        return RepositorySlicer.toSlice(meetings, pageable);
    }

    private Set<Team> extractTeam(List<Meeting> meetings) {
        Set<Team> teams = meetings.stream().map(Meeting::getOwnerTeam).map(Team.class::cast)
            .collect(Collectors.toSet());
        teams.addAll(meetings.stream().filter(meeting -> meeting.getEngagedTeam() != null)
            .map(Meeting::getEngagedTeam).map(Team.class::cast).collect(Collectors.toSet()));
        return teams;
    }

    private BooleanBuilder toWhereCondition(Student member) {
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(meetingWatchlistItem.owner.eq(member));
        return builder;
    }

}

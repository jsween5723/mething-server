package com.esc.bluespring.domain.meeting.repository;

import static com.esc.bluespring.domain.meeting.entity.QMeeting.meeting;
import static com.esc.bluespring.domain.meeting.entity.QMeetingRequest.meetingRequest;

import com.esc.bluespring.common.enums.RequestStatus;
import com.esc.bluespring.domain.meeting.classes.MeetingRequestDto.Field;
import com.esc.bluespring.domain.meeting.classes.MeetingRequestDto.SearchCondition;
import com.esc.bluespring.domain.meeting.entity.Meeting;
import com.esc.bluespring.domain.meeting.entity.MeetingRequest;
import com.esc.bluespring.domain.meeting.entity.Team;
import com.querydsl.core.group.GroupBy;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MeetingRequestQDR {
    private final JPAQueryFactory query;
    private final TeamQDR teamQDR;
    private final TeamParticipantQDR participantQDR;

    public void mapRequestsToForCount(List<Meeting> meetings) {
        Map<Meeting, List<MeetingRequest>> requestsMap = getRequestsMap(meetings);
        meetings.forEach(meeting -> meeting.mapMeetingRequests(requestsMap.get(meeting)));
    }

    public void mapRequestsToForCount(Meeting meeting) {
        mapRequestsToForCount(List.of(meeting));
    }
    private Map<Meeting, List<MeetingRequest>> getRequestsMap(List<Meeting> meetings) {
        return query.selectFrom(meetingRequest)
            .innerJoin(meetingRequest.targetMeeting, meeting)
            .leftJoin(meetingRequest.requesterTeam)
            .where(meeting.in(meetings), meetingRequest.status.eq(RequestStatus.PENDING))
            .transform(GroupBy.groupBy(meeting).as(GroupBy.list(meetingRequest)));
    }

    public List<MeetingRequest> searchByMeeting(SearchCondition condition, Pageable pageable) {
        JPAQuery<MeetingRequest> dsl = query.selectFrom(meetingRequest);
        if (condition.fields().contains(Field.requesterTeam)) {
            dsl.leftJoin(meetingRequest.requesterTeam).fetchJoin();
            teamQDR.fetchJoinTeam(dsl, meetingRequest.requesterTeam, "requester");
        }
        if (condition.fields().contains(Field.targetMeeting)) {
            dsl.leftJoin(meetingRequest.targetMeeting).fetchJoin();
            dsl.leftJoin(meetingRequest.targetMeeting.ownerTeam).fetchJoin();
            teamQDR.fetchJoinTeam(dsl, meetingRequest.targetMeeting.ownerTeam, "owner");
        }
        List<MeetingRequest> result = dsl.where(meetingRequest.status.in(condition.statuses())).fetch();
        participantQDR.mapParticipantsTo(extractTeams(result));
        return result;
    }
    private Set<Team> extractTeams(MeetingRequest request) {
        return Set.of(request.getRequesterTeam(), request.getTargetMeeting().getOwnerTeam());
    }

    private Set<Team> extractTeams(List<MeetingRequest> request) {
        return request.stream().map(this::extractTeams).flatMap(Collection::stream).collect(Collectors.toSet());
    }
}

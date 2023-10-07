package com.esc.bluespring.domain.meeting.repository;

import static com.esc.bluespring.domain.meeting.entity.QMeeting.meeting;
import static com.esc.bluespring.domain.meeting.entity.QMeetingRequest.meetingRequest;

import com.esc.bluespring.domain.meeting.entity.Meeting;
import com.esc.bluespring.domain.meeting.entity.MeetingRequest;
import com.querydsl.core.group.GroupBy;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MeetingRequestQDR {
    private final JPAQueryFactory query;

    public void mapRequestsTo(List<Meeting> meetings) {
        Map<Meeting, List<MeetingRequest>> requestsMap = getRequestsMap(meetings);
        meetings.forEach(meeting -> meeting.mapMeetingRequests(requestsMap.get(meeting)));
    }

    public void mapRequestsTo(Meeting meeting) {
        mapRequestsTo(List.of(meeting));
    }

    private Map<Meeting, List<MeetingRequest>> getRequestsMap(List<Meeting> meetings) {
        return query.selectFrom(meetingRequest)
            .innerJoin(meetingRequest.targetMeeting, meeting)
            .leftJoin(meetingRequest.requesterTeam)
            .where(meeting.in(meetings))
            .transform(GroupBy.groupBy(meeting).as(GroupBy.list(meetingRequest)));
    }
}

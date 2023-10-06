package com.esc.bluespring.domain.meeting.repository;

import static com.esc.bluespring.domain.meeting.entity.QMeetingWatchlistItem.meetingWatchlistItem;

import com.esc.bluespring.domain.meeting.entity.Meeting;
import com.esc.bluespring.domain.meeting.entity.MeetingWatchlistItem;
import com.querydsl.core.group.GroupBy;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MeetingWatchlistQDR {
    private final JPAQueryFactory query;

    public void mapWatchlistTo(List<Meeting> meetings) {
        Map<Meeting, List<MeetingWatchlistItem>> watchlistItemMap = getWatchlistItemMap(meetings);
        meetings.forEach(meeting -> meeting.mapWatchlist(watchlistItemMap.get(meeting)));
    }

    public void mapWatchlistTo(Meeting meeting) {
        Map<Meeting, List<MeetingWatchlistItem>> watchlistItemMap = getWatchlistItemMap(List.of(meeting));
        meeting.mapWatchlist(watchlistItemMap.get(meeting));
    }
    private Map<Meeting, List<MeetingWatchlistItem>> getWatchlistItemMap(List<Meeting> meetings) {
        return query.selectFrom(meetingWatchlistItem).leftJoin(meetingWatchlistItem.meeting)
            .where(meetingWatchlistItem.meeting.in(meetings)).transform(
                GroupBy.groupBy(meetingWatchlistItem.meeting)
                    .as(GroupBy.list(meetingWatchlistItem)));
    }
}

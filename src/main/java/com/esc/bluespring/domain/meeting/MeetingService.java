package com.esc.bluespring.domain.meeting;

import com.esc.bluespring.domain.meeting.classes.MeetingDto.MainPageSearchCondition;
import com.esc.bluespring.domain.meeting.entity.Meeting;
import com.esc.bluespring.domain.meeting.entity.MeetingRequest;
import com.esc.bluespring.domain.meeting.entity.MeetingWatchlistItem;
import com.esc.bluespring.domain.meeting.repository.MeetingRepository;
import com.esc.bluespring.domain.meeting.watchlist.MeetingWatchListService;
import com.esc.bluespring.domain.member.entity.Member;
import com.esc.bluespring.domain.member.entity.Student;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MeetingService {

    private final MeetingRepository repository;
    private final MeetingWatchListService watchListService;

    @Transactional
    public void save(Meeting meeting) {
        repository.save(meeting);
    }

    @Transactional(readOnly = true)
    public Meeting find(UUID id, boolean requireEngagedTeam) {
        return repository.find(id, requireEngagedTeam);
    }

    @Transactional(readOnly = true)
    public Meeting find(UUID id) {
        return find(id, false);
    }
    @Transactional(readOnly = true)
    public Slice<Meeting> searchMainPageList(Member student, MainPageSearchCondition condition, Pageable pageable) {
        return repository.searchMainPageList(student, condition, pageable);
    }

    @Transactional(readOnly = true)
    public Slice<Meeting> searchMyMeetingList(Student student, Pageable pageable) {
        return repository.searchMyMeetingList(student, pageable);
    }

    @Transactional
    public void addWatchlist(Meeting meeting, MeetingWatchlistItem item) {
        //TODO 쿼리DSL로 find쿼리 전환할 것
        meeting.addWatchListItem(item);
        save(meeting);
    }

    @Transactional(readOnly = true)
    public MeetingWatchlistItem findWatchlistItem(UUID meetingId, Member member) {
        return watchListService.find(meetingId, member);
    }

    @Transactional
    public void removeWatchlistItem(MeetingWatchlistItem entity) {
        watchListService.delete(entity);
    }

    @Transactional
    public void addRequest(Meeting meeting, MeetingRequest request) {
        meeting.addRequest(request);
        save(meeting);
    }
}

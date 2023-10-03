package com.esc.bluespring.domain.meeting;

import com.esc.bluespring.domain.meeting.classes.MeetingDto.MainPageSearchCondition;
import com.esc.bluespring.domain.meeting.entity.Meeting;
import com.esc.bluespring.domain.meeting.entity.MeetingRequest;
import com.esc.bluespring.domain.meeting.exception.MeetingException.MeetingNotFoundException;
import com.esc.bluespring.domain.meeting.repository.MeetingRepository;
import com.esc.bluespring.domain.meeting.watchlist.MeetingWatchListService;
import com.esc.bluespring.domain.meeting.watchlist.entity.MeetingWatchlistItem;
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
    public Meeting save(Meeting meeting) {
        return repository.save(meeting);
    }

    @Transactional(readOnly = true)
    public Meeting find(UUID id) {
        return repository.findById(id).orElseThrow(MeetingNotFoundException::new);
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
    public void addWatchlist(UUID meetingId, Student member) {
        //TODO 쿼리DSL로 find쿼리 전환할 것
        Meeting meeting = find(meetingId);
        meeting.appendedWatchlistBy(member);
    }

    @Transactional(readOnly = true)
    public MeetingWatchlistItem findWatchlistItem(UUID meetingOwnerTeamId, Member member) {
        return watchListService.find(meetingOwnerTeamId, member);
    }

    @Transactional
    public void removeWatchlistItem(MeetingWatchlistItem entity) {
        watchListService.delete(entity);
    }

    @Transactional
    public void addRequest(UUID meetingId, MeetingRequest request) {
        Meeting meeting = find(meetingId);
        meeting.addRequest(request);
    }
}

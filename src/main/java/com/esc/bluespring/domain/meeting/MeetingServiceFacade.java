package com.esc.bluespring.domain.meeting;

import com.esc.bluespring.domain.meeting.classes.MeetingDto.MainPageListElement;
import com.esc.bluespring.domain.meeting.classes.MeetingDto.SearchCondition;
import com.esc.bluespring.domain.meeting.entity.Meeting;
import com.esc.bluespring.domain.meeting.exception.MeetingException.MeetingNotFoundException;
import com.esc.bluespring.domain.meeting.repository.MeetingRepository;
import com.esc.bluespring.domain.meeting.request.MeetingRequestService;
import com.esc.bluespring.domain.meeting.request.entity.MeetingRequest;
import com.esc.bluespring.domain.meeting.team.TeamServiceFacade;
import com.esc.bluespring.domain.meeting.team.entity.MeetingRequesterTeam;
import com.esc.bluespring.domain.meeting.watchlist.MeetingWatchListService;
import com.esc.bluespring.domain.meeting.watchlist.entity.MeetingWatchlistItem;
import com.esc.bluespring.domain.member.entity.Member;
import com.esc.bluespring.domain.member.entity.Student;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MeetingServiceFacade {

    private final MeetingRepository repository;
    private final MeetingWatchListService watchListService;
    private final TeamServiceFacade teamServiceFacade;
    private final MeetingRequestService requestService;

    @Transactional
    public Meeting save(Meeting meeting) {
        return repository.save(meeting);
    }

    @Transactional(readOnly = true)
    public Meeting find(Long id) {
        return repository.findById(id).orElseThrow(MeetingNotFoundException::new);
    }
    @Transactional(readOnly = true)
    public Slice<MainPageListElement> search(SearchCondition condition, Pageable pageable) {
        return repository.search(condition, pageable);
    }

    @Transactional
    public void addWatchlist(Meeting meeting, Student member) {
        member.addWatchlist(meeting);
    }

    @Transactional(readOnly = true)
    public MeetingWatchlistItem findWatchlistItem(Long meetingOwnerTeamId, Member member) {
        return watchListService.find(meetingOwnerTeamId, member);
    }

    @Transactional
    public void removeWatchlistItem(MeetingWatchlistItem entity) {
        watchListService.delete(entity);
    }

    @Transactional
    public void addRequest(Meeting meeting, MeetingRequesterTeam requester) {
        teamServiceFacade.joinRequest(meeting, requester);
    }

    @Transactional
    public void acceptRequest(MeetingRequest request) {
        Meeting meeting = requestService.accept(request);
        repository.save(meeting);
    }
}

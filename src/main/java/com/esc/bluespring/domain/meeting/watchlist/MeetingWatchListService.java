package com.esc.bluespring.domain.meeting.watchlist;

import com.esc.bluespring.domain.meeting.entity.MeetingWatchlistItem;
import com.esc.bluespring.domain.meeting.watchlist.exception.MeetingWatchlistException.MeetingWatchlistItemNotFoundException;
import com.esc.bluespring.domain.member.entity.Member;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MeetingWatchListService {

    private final MeetingWatchListRepository repository;

    @Transactional
    public MeetingWatchlistItem save(MeetingWatchlistItem entity) {
        return repository.save(entity);
    }

    @Transactional
    public MeetingWatchlistItem find(UUID meetingId, Member member) {
        return repository.findByMeeting_IdAndOwner(meetingId, member)
            .orElseThrow(MeetingWatchlistItemNotFoundException::new);
    }

    @Transactional
    public void delete(MeetingWatchlistItem entity) {
        repository.delete(entity);
    }
}

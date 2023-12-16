package com.esc.bluespring.domain.meeting.watchlist;

import com.esc.bluespring.domain.meeting.entity.Meeting;
import com.esc.bluespring.domain.meeting.entity.MeetingWatchlistItem;
import com.esc.bluespring.domain.meeting.watchlist.exception.MeetingWatchlistException.MeetingWatchlistItemNotFoundException;
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
public class MeetingWatchListService {

    private final MeetingWatchListRepository repository;
    private final MeetingWatchListReadRepository readRepository;

    @Transactional
    public MeetingWatchlistItem find(UUID meetingId, Member member) {
        return repository.findByMeeting_IdAndOwner(meetingId, member)
            .orElseThrow(MeetingWatchlistItemNotFoundException::new);
    }

    @Transactional(readOnly = true)
    public Slice<Meeting> getMyWatchlist(Student student, Pageable pageable) {
        return readRepository.getMyList(student, pageable);
    }

    @Transactional
    public void delete(MeetingWatchlistItem entity) {
        repository.delete(entity);
    }
}

package com.esc.bluespring.domain.meeting.watchlist;

import com.esc.bluespring.domain.meeting.watchlist.entity.MeetingWatchlistItem;
import com.esc.bluespring.domain.member.entity.Member;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MeetingWatchListRepository extends JpaRepository<MeetingWatchlistItem, Long> {
    Optional<MeetingWatchlistItem> findByMeeting_IdAndOwner(Long id, Member owner);
}

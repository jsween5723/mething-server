package com.esc.bluespring.domain.meeting.watchlist;

import com.esc.bluespring.domain.meeting.entity.MeetingWatchlistItem;
import com.esc.bluespring.domain.member.entity.Member;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MeetingWatchListRepository extends JpaRepository<MeetingWatchlistItem, UUID> {
    Optional<MeetingWatchlistItem> findByMeeting_IdAndOwner(UUID id, Member owner);
}

package com.esc.bluespring.domain.meeting.entity;

import com.esc.bluespring.common.entity.OwnerEntity;
import com.esc.bluespring.domain.member.entity.Student;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Embeddable;
import jakarta.persistence.OneToMany;
import java.util.LinkedHashSet;
import java.util.Set;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor
public class MeetingWatchlist {

    public MeetingWatchlist(Set<MeetingWatchlistItem> watchlist) {
        this.watchlist = watchlist;
    }

    @OneToMany(mappedBy = "meeting", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<MeetingWatchlistItem> watchlist = new LinkedHashSet<>();

    public void addWatchlistItem(MeetingWatchlistItem meetingWatchlistItem) {
        watchlist.add(meetingWatchlistItem);
    }

    public boolean isMembersWatchlist(Student student) {
        return watchlist.stream().map(OwnerEntity::getOwner).toList().contains(student);
    }

    public int getLikeCount() {
        return this.watchlist.size();
    }
}

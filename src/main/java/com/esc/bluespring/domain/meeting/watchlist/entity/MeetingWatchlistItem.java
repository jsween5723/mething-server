package com.esc.bluespring.domain.meeting.watchlist.entity;

import com.esc.bluespring.common.entity.OwnerEntity;
import com.esc.bluespring.domain.meeting.entity.Meeting;
import com.esc.bluespring.domain.member.entity.Student;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "meeting_watchlist_elements")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MeetingWatchlistItem extends OwnerEntity {
    @JoinColumn(name = "meeting_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Meeting meeting;

    @Builder
    MeetingWatchlistItem(Long id, Student owner, Meeting meeting) {
        super(id, owner);
        this.meeting = meeting;
    }
}

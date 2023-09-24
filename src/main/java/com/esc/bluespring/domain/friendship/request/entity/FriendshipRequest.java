package com.esc.bluespring.domain.friendship.request.entity;

import com.esc.bluespring.common.entity.BaseEntity;
import com.esc.bluespring.common.enums.RequestStatus;
import com.esc.bluespring.common.exception.RequestException.NotOwnerException;
import com.esc.bluespring.domain.member.entity.Member;
import com.esc.bluespring.domain.member.entity.Student;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "friend_requests")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FriendshipRequest extends BaseEntity {

    @JoinColumn(name = "requester_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Student requester;

    @JoinColumn(name = "target_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Student target;

    private String message;

    @Enumerated(EnumType.STRING)
    private RequestStatus status = RequestStatus.PENDING;

    @Builder
    FriendshipRequest(Long id, Student requester, Student target, String message) {
        super(id);
        this.requester = requester;
        this.target = target;
        this.message = message;
    }

    public void accept() {
        status = RequestStatus.ACCEPTED;
        requester.addFriendship(target);
        target.addFriendship(requester);
    }

    public void reject() {
        status = RequestStatus.REJECTED;
    }

    public void validRequesterOwner(Member user) {
        if (user.isAdmin()) {
            return;
        }
        if (requester.getId().equals(user.getId())) {
            throw new NotOwnerException();
        }
    }

    public void validTargetOwner(Member user) {
        if (user.isAdmin()) {
            return;
        }
        if (target.getId().equals(user.getId())) {
            throw new NotOwnerException();
        }
    }
}

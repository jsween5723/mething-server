package com.esc.bluespring.domain.friendship.request.classes;

import com.esc.bluespring.domain.member.classes.MemberDto;
import lombok.Builder;

public record FriendshipRequestDto() {
    public record SearchCondition(String universityName, String requesterNickname, String targetNickname, Long targetId) {
        public SearchCondition(String universityName, String requesterNickname, Long targetId) {
            this(universityName,requesterNickname, null, targetId);
        }
    }

    public record ListElement(Long id, MemberDto.Detail requester, String message) {

        @Builder
        public ListElement {
        }
    }
}

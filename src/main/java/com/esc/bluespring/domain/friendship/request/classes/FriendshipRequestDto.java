package com.esc.bluespring.domain.friendship.request.classes;

import com.esc.bluespring.domain.member.classes.MemberDto;
import java.util.UUID;
import lombok.Builder;

public record FriendshipRequestDto() {
    public record SearchCondition(String universityName, String requesterNickname, String targetNickname, UUID targetId) {
        public SearchCondition(String universityName, String requesterNickname, UUID targetId) {
            this(universityName,requesterNickname, null, targetId);
        }
    }

    public record ListElement(UUID id, MemberDto.Detail requester, String message) {

        @Builder
        public ListElement {
        }
    }
}

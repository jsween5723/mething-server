package com.esc.bluespring.domain.friendship.classes;

import com.esc.bluespring.domain.member.classes.MemberDto;
import lombok.Builder;

public record FriendshipDto() {
    public record SearchCondition(Long universityId, String universityName, String locationDistrictName, String nickname, Long memberId) {}
    public record ListElement(Long id, MemberDto.Detail friend, long createdAt) {
        @Builder
        public ListElement {}
    }
}

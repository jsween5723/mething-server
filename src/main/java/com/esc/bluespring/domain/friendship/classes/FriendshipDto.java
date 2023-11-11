package com.esc.bluespring.domain.friendship.classes;

import com.esc.bluespring.domain.member.student.classes.StudentDto;
import java.util.UUID;
import lombok.Builder;

public record FriendshipDto() {
    public record SearchCondition(Long universityId, String universityName, String locationDistrictName, String nickname, UUID memberId) {}
    public record ListElement(UUID id, StudentDto.ListElement friend, long createdAt) {
        @Builder
        public ListElement {}
    }
}

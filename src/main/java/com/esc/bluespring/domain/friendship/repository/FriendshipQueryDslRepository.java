package com.esc.bluespring.domain.friendship.repository;

import com.esc.bluespring.domain.friendship.classes.FriendshipDto.SearchCondition;
import com.esc.bluespring.domain.friendship.entity.Friendship;
import com.esc.bluespring.domain.member.entity.Member;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface FriendshipQueryDslRepository {
    Slice<Friendship> search(SearchCondition condition, Member user, Pageable pageable);
}

package com.esc.bluespring.domain.friendship.request.repository;

import com.esc.bluespring.domain.friendship.request.classes.FriendshipRequestDto.SearchCondition;
import com.esc.bluespring.domain.friendship.request.entity.FriendshipRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

interface FriendshipRequestQueryDslRepository {
    Slice<FriendshipRequest> search(SearchCondition condition, Pageable pageable);
}

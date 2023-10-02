package com.esc.bluespring.domain.friendship.repository;

import com.esc.bluespring.domain.friendship.entity.Friendship;
import com.esc.bluespring.domain.member.entity.Member;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FriendshipRepository extends JpaRepository<Friendship, UUID>, FriendshipQueryDslRepository{
    Optional<Friendship> findByMemberAndFriend(Member member, Member friend);
}

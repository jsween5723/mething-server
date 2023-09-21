package com.esc.bluespring.domain.friendship;

import com.esc.bluespring.domain.friendship.classes.FriendshipDto.SearchCondition;
import com.esc.bluespring.domain.friendship.entity.Friendship;
import com.esc.bluespring.domain.friendship.exception.FriendshipException.FriendshipNotFoundException;
import com.esc.bluespring.domain.friendship.repository.FriendshipRepository;
import com.esc.bluespring.domain.member.entity.Member;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class FriendshipService {

    private final FriendshipRepository repository;

    public Slice<Friendship> search(SearchCondition condition, Pageable pageable, Member user) {
        return repository.search(condition, user, pageable);
    }

    public Friendship find(Long id) {
        return repository.findById(id).orElseThrow(FriendshipNotFoundException::new);
    }

    public Friendship find(Member member, Member friend) {
        return repository.findByMemberAndFriend(member, friend)
            .orElseThrow(FriendshipNotFoundException::new);
    }

    public void deleteFriendship(Friendship friendship) {
        Friendship opponentRelation = find(friendship.getFriend(), friendship.getMember());
        repository.deleteAll(List.of(opponentRelation, friendship));
    }
}

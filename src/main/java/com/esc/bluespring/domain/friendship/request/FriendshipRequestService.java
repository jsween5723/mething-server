package com.esc.bluespring.domain.friendship.request;

import com.esc.bluespring.common.exception.RequestException.RequestNotFoundException;
import com.esc.bluespring.domain.friendship.request.classes.FriendshipRequestDto.SearchCondition;
import com.esc.bluespring.domain.friendship.request.entity.FriendshipRequest;
import com.esc.bluespring.domain.friendship.request.repository.FriendshipRequestRequestRepository;
import com.esc.bluespring.domain.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class FriendshipRequestService {
    private final FriendshipRequestRequestRepository repository;

    @Transactional(readOnly = true)
    public FriendshipRequest find(Long id) {
        return repository.find(id).orElseThrow(RequestNotFoundException::new);
    }

    @Transactional(readOnly = true)
    public Slice<FriendshipRequest> search(SearchCondition condition, Pageable pageable) {
        return repository.search(condition, pageable);
    }

    public FriendshipRequest save(FriendshipRequest request) {
        return repository.save(request);
    }

    public void accept(FriendshipRequest request, Member user) {
        request.validTargetOwner(user);
        request.accept();
    }

    public void reject(FriendshipRequest request, Member user) {
        request.validTargetOwner(user);
        request.reject();
    }

    public void delete(FriendshipRequest request, Member user) {
        request.validRequesterOwner(user);
        repository.delete(request);
    }
}

package com.esc.bluespring.domain.friendship.request.repository;

import com.esc.bluespring.domain.friendship.request.entity.FriendshipRequest;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface FriendshipRequestRequestRepository extends JpaRepository<FriendshipRequest, Long>,
    FriendshipRequestQueryDslRepository {
    @Query("select fsr from FriendshipRequest fsr "
        + "join fetch fsr.target t join fetch t.schoolInformation.major tm "
        + "join fetch tm.university tu join tu.locationDistrict "
        + "join fetch fsr.requester r join fetch r.schoolInformation.major rm "
        + "join fetch rm.university ru join fetch ru.locationDistrict "
        + "where fsr.id=:id")
    Optional<FriendshipRequest> find(Long id);
}

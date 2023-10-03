package com.esc.bluespring.domain.meeting.repository;

import com.esc.bluespring.domain.meeting.entity.Meeting;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MeetingRepository extends JpaRepository<Meeting, UUID>, MeetingQDR {

    @Override
    @EntityGraph(attributePaths = {"ownerTeam"})
    Optional<Meeting> findById(UUID aLong);
}

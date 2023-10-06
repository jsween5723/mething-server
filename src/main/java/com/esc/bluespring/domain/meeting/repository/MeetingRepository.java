package com.esc.bluespring.domain.meeting.repository;

import com.esc.bluespring.domain.meeting.entity.Meeting;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MeetingRepository extends JpaRepository<Meeting, UUID>, MeetingQDR {}

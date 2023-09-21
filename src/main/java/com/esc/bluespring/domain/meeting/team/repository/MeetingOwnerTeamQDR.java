package com.esc.bluespring.domain.meeting.team.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class MeetingOwnerTeamQDR {
    private final JPAQueryFactory query;

}

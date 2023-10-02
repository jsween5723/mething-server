package com.esc.bluespring.domain.meeting.team.repository;

import com.esc.bluespring.domain.meeting.team.classes.TeamDto;
import com.esc.bluespring.domain.meeting.team.classes.TeamDto.MainPageListElement;
import com.esc.bluespring.domain.meeting.team.entity.QMeetingOwnerTeam;
import com.esc.bluespring.domain.university.repository.UniversityQDRImpl;
import com.querydsl.core.types.ConstructorExpression;
import com.querydsl.core.types.Projections;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class MeetingOwnerTeamQDR {

    private final UniversityQDRImpl universityQDR;

    public ConstructorExpression<TeamDto.MainPageListElement> toMainPageListElement(
        QMeetingOwnerTeam team) {
        return Projections.constructor(MainPageListElement.class, team.id, team.title,
            universityQDR.toMainPageListElement(team.representedUniversity), team.participants);
    }
}

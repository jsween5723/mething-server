package com.esc.bluespring.domain.meeting.repository;

import static com.esc.bluespring.domain.meeting.entity.QTeam.team;
import static com.esc.bluespring.domain.meeting.entity.QTeamParticipant.teamParticipant;

import com.esc.bluespring.domain.meeting.entity.Team;
import com.esc.bluespring.domain.meeting.entity.TeamParticipant;
import com.esc.bluespring.domain.member.entity.QStudent;
import com.esc.bluespring.domain.member.student.repository.StudentQDRImpl;
import com.querydsl.core.group.GroupBy;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.Map;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TeamParticipantQDR {

  private final JPAQueryFactory query;
  private final StudentQDRImpl studentQDR;
  public void mapParticipantsTo(Set<Team> teams) {
    Map<Team, Set<TeamParticipant>> participantsMap = getParticipantsMap(teams);
    teams.forEach(team -> team.mapParticipants(participantsMap.get(team)));
  }
  private Map<Team, Set<TeamParticipant>> getParticipantsMap(Set<Team> teams) {
    QStudent student = new QStudent("map");
    JPAQuery<TeamParticipant> dsl = query.selectFrom(teamParticipant)
        .leftJoin(teamParticipant.team, team)
        .leftJoin(teamParticipant.participant, student).fetchJoin();
    studentQDR.fetchJoinStudent(dsl, student, "teamParticipant");
    return dsl.where(team.in(teams))
        .transform(GroupBy.groupBy(team).as(GroupBy.set(teamParticipant)));
  }
}

package com.esc.bluespring.domain.meeting.repository;

import static com.esc.bluespring.domain.meeting.entity.QTeam.team;
import static com.esc.bluespring.domain.meeting.entity.QTeamParticipant.teamParticipant;
import static com.esc.bluespring.domain.member.entity.QStudent.student;

import com.esc.bluespring.domain.meeting.entity.Meeting;
import com.esc.bluespring.domain.meeting.entity.Team;
import com.esc.bluespring.domain.meeting.entity.TeamParticipant;
import com.esc.bluespring.domain.member.student.repository.StudentQDRImpl;
import com.querydsl.core.group.GroupBy;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TeamParticipantQDR {

  private final JPAQueryFactory query;
  private final StudentQDRImpl studentQDR;

  public void mapParticipantsTo(List<Meeting> meetings) {
    List<Team> teams = new ArrayList<>(
        meetings.stream().map(Meeting::getOwnerTeam).map(team -> (Team) team).toList());
    teams.addAll(meetings.stream().filter(meeting -> meeting.getEngagedTeam() != null)
        .map(Meeting::getEngagedTeam).map(team -> (Team) team).toList());
    Map<Team, List<TeamParticipant>> participantsMap = getParticipantsMap(teams);
    meetings.forEach(meeting -> meeting.getOwnerTeam()
        .mapParticipants(participantsMap.get(meeting.getOwnerTeam())));
  }

  public void mapParticipantsTo(Meeting meeting) {
    mapParticipantsTo(List.of(meeting));
  }

  private Map<Team, List<TeamParticipant>> getParticipantsMap(List<Team> teams) {
    JPAQuery<TeamParticipant> dsl = query.selectFrom(teamParticipant)
        .leftJoin(teamParticipant.team, team)
        .leftJoin(teamParticipant.member, student).fetchJoin();
    studentQDR.fetchJoinStudent(dsl, student, "teamParticipant");
    return dsl.where(team.in(teams))
        .transform(GroupBy.groupBy(team).as(GroupBy.list(teamParticipant)));
  }
}

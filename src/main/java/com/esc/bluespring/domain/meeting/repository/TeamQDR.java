package com.esc.bluespring.domain.meeting.repository;

import static com.esc.bluespring.domain.locationDistrict.entity.QLocationDistrict.locationDistrict;

import com.esc.bluespring.domain.meeting.entity.QMeetingOwnerTeam;
import com.esc.bluespring.domain.meeting.entity.QMeetingRequesterTeam;
import com.esc.bluespring.domain.member.entity.QStudent;
import com.esc.bluespring.domain.member.student.repository.StudentQDRImpl;
import com.esc.bluespring.domain.university.entity.QUniversity;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TeamQDR {
  private final JPAQueryFactory query;
  private final StudentQDRImpl studentQDR;

  public void fetchJoinTeam(JPAQuery query, QMeetingOwnerTeam team,
                             String key) {
    QStudent owner = new QStudent(key);
    QUniversity qUniversity = new QUniversity(key + "university");
    query.leftJoin(team.representedUniversity, qUniversity).fetchJoin()
        .leftJoin(qUniversity.locationDistrict, locationDistrict).fetchJoin()
        .leftJoin(team.owner, owner).fetchJoin();
    studentQDR.fetchJoinStudent(query, owner, key + "owner");
  }

  public void fetchJoinTeam(JPAQuery query, QMeetingRequesterTeam team,
                                          String key) {
    QStudent owner = new QStudent(key);
    QUniversity qUniversity = new QUniversity(key + "university");
    query.leftJoin(team.representedUniversity, qUniversity).fetchJoin()
        .leftJoin(qUniversity.locationDistrict).fetchJoin()
        .leftJoin(team.owner, owner).fetchJoin();
    studentQDR.fetchJoinStudent(query, owner, key + "owner");
  }
}

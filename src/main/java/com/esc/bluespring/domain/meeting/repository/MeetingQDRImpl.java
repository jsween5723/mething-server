package com.esc.bluespring.domain.meeting.repository;

import static com.esc.bluespring.domain.locationDistrict.entity.QLocationDistrict.locationDistrict;
import static com.esc.bluespring.domain.meeting.entity.QMeeting.meeting;
import static com.esc.bluespring.domain.meeting.entity.QMeetingOwnerTeam.meetingOwnerTeam;
import static com.esc.bluespring.domain.meeting.entity.QMeetingRequesterTeam.meetingRequesterTeam;
import static com.esc.bluespring.domain.meeting.entity.QTeamParticipant.teamParticipant;
import static com.esc.bluespring.domain.member.entity.QStudent.student;
import static com.esc.bluespring.domain.university.entity.QUniversity.university;

import com.esc.bluespring.common.utils.querydsl.RepositorySlicer;
import com.esc.bluespring.domain.meeting.classes.MeetingDto.MainPageSearchCondition;
import com.esc.bluespring.domain.meeting.entity.Meeting;
import com.esc.bluespring.domain.meeting.entity.QMeetingOwnerTeam;
import com.esc.bluespring.domain.member.entity.Member;
import com.esc.bluespring.domain.member.entity.QStudent;
import com.esc.bluespring.domain.member.entity.Student;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
public class MeetingQDRImpl implements MeetingQDR {

    private final JPAQueryFactory query;
    private final MeetingRequestQDR requestQDR;
    private final MeetingWatchlistQDR watchlistQDR;
    private final TeamParticipantQDR participantQDR;

    @Transactional(readOnly = true)
    public Slice<Meeting> searchMainPageList(Member user, MainPageSearchCondition condition,
        Pageable pageable) {
        List<Meeting> meetings = query.selectFrom(meeting)
            .leftJoin(meeting.ownerTeam, meetingOwnerTeam).fetchJoin()
            .leftJoin(meetingOwnerTeam.owner.as(QStudent.class), student).fetchJoin()
            .leftJoin(meetingOwnerTeam.representedUniversity, university).fetchJoin()
            .leftJoin(university.locationDistrict, locationDistrict).fetchJoin().fetchJoin()
            .where(meeting.engagedTeam.isNull(), toWhereCondition(condition, user)).offset(pageable.getOffset())
            .limit(pageable.getPageSize() + 1).fetch();
        participantQDR.mapParticipantsTo(meetings);
        watchlistQDR.mapWatchlistTo(meetings);
        return RepositorySlicer.toSlice(meetings, pageable);
    }
    @Transactional(readOnly = true)
    public Meeting find(UUID id) {
        QStudent owner = new QStudent("owner");
        QStudent requester = new QStudent("requester");
        Meeting result = query.selectFrom(meeting)
            .leftJoin(meeting.ownerTeam, meetingOwnerTeam).fetchJoin()
            .leftJoin(meetingOwnerTeam.owner.as(QStudent.class), owner).fetchJoin()
            .leftJoin(owner.schoolInformation.major).fetchJoin()
            .leftJoin(owner.schoolInformation.major.university).fetchJoin()
            .leftJoin(owner.schoolInformation.major.university.locationDistrict).fetchJoin()
            .leftJoin(meetingOwnerTeam.representedUniversity, university).fetchJoin()
            .leftJoin(university.locationDistrict, locationDistrict).fetchJoin().fetchJoin()
            .leftJoin(meeting.engagedTeam, meetingRequesterTeam).fetchJoin()
            .leftJoin(meetingRequesterTeam.owner.as(QStudent.class), requester).fetchJoin()
            .leftJoin(requester.schoolInformation.major).fetchJoin()
            .leftJoin(requester.schoolInformation.major.university).fetchJoin()
            .leftJoin(requester.schoolInformation.major.university.locationDistrict).fetchJoin()
            .leftJoin(meetingRequesterTeam.representedUniversity, university).fetchJoin()
            .leftJoin(university.locationDistrict, locationDistrict).fetchJoin().fetchJoin()
            .where(meeting.id.eq(id))
            .fetchFirst();
        participantQDR.mapParticipantsTo(result);
        watchlistQDR.mapWatchlistTo(result);
        requestQDR.mapRequestsTo(result);
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public Slice<Meeting> searchMyMeetingList(Student user, Pageable pageable) {
        List<Meeting> meetings = query.selectFrom(meeting)
            .leftJoin(meeting.ownerTeam.as(QMeetingOwnerTeam.class), meetingOwnerTeam).fetchJoin()
            .leftJoin(meetingOwnerTeam.participants, teamParticipant)
            .leftJoin(meetingOwnerTeam.owner.as(QStudent.class), student).fetchJoin()
            .leftJoin(meetingOwnerTeam.representedUniversity, university).fetchJoin()
            .leftJoin(university.locationDistrict, locationDistrict).fetchJoin()
            .where(meeting.engagedTeam.isNull(),
                meetingOwnerTeam.owner.eq(user).or(teamParticipant.member.eq(user)))
            .offset(pageable.getOffset()).limit(pageable.getPageSize() + 1).fetch();
        participantQDR.mapParticipantsTo(meetings);
        requestQDR.mapRequestsTo(meetings);
        return RepositorySlicer.toSlice(meetings, pageable);
    }

    private BooleanBuilder toWhereCondition(MainPageSearchCondition condition, Member member) {
        BooleanBuilder builder = new BooleanBuilder();
        if (condition.isMyLocation() && member instanceof Student student) {
            builder.and(locationDistrict.id.eq(
                student.getSchoolInformation().getMajor().getUniversity().getLocationDistrict()
                    .getId()));
        }
        return builder;
    }
}

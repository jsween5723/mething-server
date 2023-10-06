package com.esc.bluespring.domain.meeting.repository;

import com.esc.bluespring.domain.meeting.classes.MeetingDto.MainPageSearchCondition;
import com.esc.bluespring.domain.meeting.entity.Meeting;
import com.esc.bluespring.domain.member.entity.Member;
import com.esc.bluespring.domain.member.entity.Student;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface MeetingQDR {

    Slice<Meeting> searchMainPageList(Member student, MainPageSearchCondition condition, Pageable pageable);
    Slice<Meeting> searchMyMeetingList(Student student, Pageable pageable);
    Meeting find(UUID id);
}

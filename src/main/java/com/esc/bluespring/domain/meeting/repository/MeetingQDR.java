package com.esc.bluespring.domain.meeting.repository;

import com.esc.bluespring.domain.meeting.classes.MeetingDto.MainPageListElement;
import com.esc.bluespring.domain.meeting.classes.MeetingDto.SearchCondition;
import com.esc.bluespring.domain.member.entity.Student;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface MeetingQDR {

    Slice<MainPageListElement> search(Student student, SearchCondition condition,
        Pageable pageable);
}

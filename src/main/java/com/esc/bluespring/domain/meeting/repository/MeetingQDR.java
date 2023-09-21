package com.esc.bluespring.domain.meeting.repository;

import com.esc.bluespring.domain.meeting.classes.MeetingDto.MainPageListElement;
import com.esc.bluespring.domain.meeting.classes.MeetingDto.SearchCondition;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface MeetingQDR {
    Slice<MainPageListElement> search(SearchCondition condition, Pageable pageable);
}

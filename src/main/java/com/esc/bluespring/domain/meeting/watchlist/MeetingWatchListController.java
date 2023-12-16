package com.esc.bluespring.domain.meeting.watchlist;

import com.esc.bluespring.common.BaseResponse;
import com.esc.bluespring.common.CustomSlice;
import com.esc.bluespring.domain.meeting.classes.MeetingDto.MainPageListElement;
import com.esc.bluespring.domain.meeting.mapper.MeetingMapper;
import com.esc.bluespring.domain.member.entity.Student;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/meeting-watchlist-items")
@RequiredArgsConstructor
public class MeetingWatchListController {
    private final MeetingWatchListService service;
    private final MeetingMapper mapper = MeetingMapper.INSTANCE;
    @GetMapping("me")
    public BaseResponse<CustomSlice<MainPageListElement>> getMyWatchlist(Student student, Pageable pageable) {
        Slice<MainPageListElement> list = service.getMyWatchlist(student, pageable)
            .map(item->mapper.toMainPageListElement(item, student));
        return new BaseResponse<>(new CustomSlice<>(list));
    }
}

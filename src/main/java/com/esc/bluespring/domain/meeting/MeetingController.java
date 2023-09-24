package com.esc.bluespring.domain.meeting;

import com.esc.bluespring.common.CustomSlice;
import com.esc.bluespring.domain.meeting.classes.MeetingDto;
import com.esc.bluespring.domain.meeting.classes.MeetingDto.Create;
import com.esc.bluespring.domain.meeting.classes.MeetingDto.MainPageListElement;
import com.esc.bluespring.domain.meeting.classes.MeetingDto.SearchCondition;
import com.esc.bluespring.domain.meeting.entity.Meeting;
import com.esc.bluespring.domain.meeting.mapper.MeetingMapper;
import com.esc.bluespring.domain.meeting.mapper.TeamMapper;
import com.esc.bluespring.domain.meeting.team.entity.MeetingRequesterTeam;
import com.esc.bluespring.domain.meeting.watchlist.entity.MeetingWatchlistItem;
import com.esc.bluespring.domain.member.entity.Member;
import com.esc.bluespring.domain.member.entity.Student;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/meetings")
@RequiredArgsConstructor
public class MeetingController {

    private final MeetingMapper meetingMapper;
    private final TeamMapper teamMapper;
    private final MeetingServiceFacade meetingServiceFacade;

    @GetMapping
    public CustomSlice<MainPageListElement> search(SearchCondition condition, Student student,
        Pageable pageable) {
        Slice<MainPageListElement> result = meetingServiceFacade.search(student, condition,
            pageable);
        return new CustomSlice<>(result);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@Valid @RequestBody Create dto, Student member) {
        Meeting entity = meetingMapper.toEntity(dto, member);
        meetingServiceFacade.save(entity);
    }

    @PostMapping("{id}/requests")
    @ResponseStatus(HttpStatus.CREATED)
    public void request(@PathVariable Long id, @Valid @RequestBody MeetingDto.Request dto,
        Student member) {
        Meeting meeting = meetingServiceFacade.find(id);
        MeetingRequesterTeam meetingRequesterTeam = teamMapper.toEntity(dto, member);
        meetingServiceFacade.addRequest(meeting, meetingRequesterTeam);
    }

    @PostMapping("{id}/watchlist-items/add")
    @ResponseStatus(HttpStatus.CREATED)
    public void addWatchlist(@PathVariable Long id, Student member) {
        Meeting meeting = meetingServiceFacade.find(id);
        meetingServiceFacade.addWatchlist(meeting, member);
    }

    @DeleteMapping("{id}/watchlist-items/remove")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void takeOutFromWatchlist(@PathVariable Long id, Member member) {
        MeetingWatchlistItem meetingWatchlistItem = meetingServiceFacade.findWatchlistItem(id,
            member);
        meetingWatchlistItem.validOwner(member);
        meetingServiceFacade.removeWatchlistItem(meetingWatchlistItem);
    }
}

package com.esc.bluespring.domain.meeting;

import com.esc.bluespring.common.CustomSlice;
import com.esc.bluespring.common.resolver.annotation.AllowAnonymous;
import com.esc.bluespring.domain.meeting.classes.MeetingDto;
import com.esc.bluespring.domain.meeting.classes.MeetingDto.Create;
import com.esc.bluespring.domain.meeting.classes.MeetingDto.MainPageListElement;
import com.esc.bluespring.domain.meeting.classes.MeetingDto.MyMeetingPageListElement;
import com.esc.bluespring.domain.meeting.classes.MeetingDto.MainPageSearchCondition;
import com.esc.bluespring.domain.meeting.entity.Meeting;
import com.esc.bluespring.domain.meeting.mapper.MeetingMapper;
import com.esc.bluespring.domain.meeting.mapper.TeamMapper;
import com.esc.bluespring.domain.meeting.team.entity.MeetingRequesterTeam;
import com.esc.bluespring.domain.meeting.watchlist.entity.MeetingWatchlistItem;
import com.esc.bluespring.domain.member.entity.Member;
import com.esc.bluespring.domain.member.entity.Student;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
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
@Tag(name = "미팅 컨트롤러")
public class MeetingController {
    private final MeetingMapper meetingMapper = MeetingMapper.INSTANCE;
    private final TeamMapper teamMapper = TeamMapper.INSTANCE;
    private final MeetingServiceFacade meetingServiceFacade;

    @GetMapping
    @Operation(description = "메인 화면 과팅 목록", parameters = @Parameter(in = ParameterIn.HEADER, name = "Authorization"))
    public CustomSlice<MainPageListElement> search(@ParameterObject MainPageSearchCondition condition,
        @AllowAnonymous Student student, Pageable pageable) {
        Slice<MainPageListElement> result = meetingServiceFacade.searchMainPageList(student, condition,
            pageable).map(meeting -> meetingMapper.toMainPageListElement(meeting, student));
        return new CustomSlice<>(result);
    }

    @GetMapping("/me")
    @Operation(description = "내 과팅 목록", parameters = @Parameter(required = true, in = ParameterIn.HEADER, name = "Authorization"))
    public CustomSlice<MyMeetingPageListElement> search(Student student, Pageable pageable) {
        Slice<MyMeetingPageListElement> result = meetingServiceFacade.searchMyMeetingList(student,
            pageable).map(meetingMapper::toMyMeetingPageListElement);
        return new CustomSlice<>(result);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(description = "과팅 생성", parameters = @Parameter(required = true, in = ParameterIn.HEADER, name = "Authorization"))
    public void create(@Valid @RequestBody Create dto, Student member) {
        Meeting entity = meetingMapper.toEntity(dto, member);
        meetingServiceFacade.save(entity);
    }

    @PostMapping("{id}/requests")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(description = "특정 과팅 신청 목록", parameters = @Parameter(required = true, in = ParameterIn.HEADER, name = "Authorization"))
    public void request(@PathVariable Long id, @Valid @RequestBody MeetingDto.Request dto,
        Student member) {
        Meeting meeting = meetingServiceFacade.find(id);
        MeetingRequesterTeam meetingRequesterTeam = teamMapper.toEntity(dto, member);
        meetingServiceFacade.addRequest(meeting, meetingRequesterTeam);
    }

    @PostMapping("{id}/watchlist-items/add")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(description = "메인 화면 미팅 목록", parameters = @Parameter(required = true, in = ParameterIn.HEADER, name = "Authorization"))
    public void addWatchlist(@PathVariable Long id, Student member) {
        Meeting meeting = meetingServiceFacade.find(id);
        meetingServiceFacade.addWatchlist(meeting, member);
    }

    @DeleteMapping("{id}/watchlist-items/remove")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(description = "메인 화면 미팅 목록", parameters = @Parameter(required = true, in = ParameterIn.HEADER, name = "Authorization"))
    public void takeOutFromWatchlist(@PathVariable Long id, Member member) {
        MeetingWatchlistItem meetingWatchlistItem = meetingServiceFacade.findWatchlistItem(id,
            member);
        meetingWatchlistItem.validOwner(member);
        meetingServiceFacade.removeWatchlistItem(meetingWatchlistItem);
    }
}

package com.esc.bluespring.domain.meeting;

import static com.esc.bluespring.domain.member.entity.Member.ADMIN;
import static com.esc.bluespring.domain.member.entity.Member.STUDENT;

import com.esc.bluespring.common.CustomSlice;
import com.esc.bluespring.domain.auth.exception.AuthException.ForbiddenException;
import com.esc.bluespring.domain.meeting.classes.MeetingDto;
import com.esc.bluespring.domain.meeting.classes.MeetingDto.Create;
import com.esc.bluespring.domain.meeting.classes.MeetingDto.Detail;
import com.esc.bluespring.domain.meeting.classes.MeetingDto.ListElement;
import com.esc.bluespring.domain.meeting.classes.MeetingDto.MainPageListElement;
import com.esc.bluespring.domain.meeting.classes.MeetingDto.SearchCondition;
import com.esc.bluespring.domain.meeting.classes.MeetingDto.MyMeetingPageListElement;
import com.esc.bluespring.domain.meeting.classes.MeetingRequestDto;
import com.esc.bluespring.domain.meeting.entity.Meeting;
import com.esc.bluespring.domain.meeting.entity.MeetingRequest;
import com.esc.bluespring.domain.meeting.entity.MeetingWatchlistItem;
import com.esc.bluespring.domain.meeting.mapper.MeetingMapper;
import com.esc.bluespring.domain.member.entity.Member;
import com.esc.bluespring.domain.member.entity.Student;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
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
  private final MeetingService meetingService;

  @GetMapping
  @Operation(description = "메인 화면 과팅 목록", parameters = @Parameter(in = ParameterIn.HEADER, name = "Authorization"))
  public CustomSlice<MainPageListElement> search(@ParameterObject SearchCondition condition,
                                                 Member user, @ParameterObject Pageable pageable) {
    if (!(user instanceof Student) && condition.isMyLocation() != null && condition.isMyLocation()) {
      throw new ForbiddenException();
    }
    Slice<MainPageListElement> result = meetingService.searchMainPageList(user, condition, pageable)
        .map(meeting -> meetingMapper.toMainPageListElement(meeting,
            user instanceof Student student ? student : null));
    return new CustomSlice<>(result);
  }

  @GetMapping("list")
  @Operation(description = "id 검색")
  public List<ListElement> getList(SearchCondition condition) {
    List<Meeting> list = meetingService.getList(condition.ids());
    return list.stream().map(meetingMapper::toListElement).toList();
  }

  @GetMapping("{id}")
  public Detail getDetail(@PathVariable UUID id, Member user,
                          @Parameter(description = "채팅 서버용 파라미터") Boolean requireEngagedTeam) {
    Meeting meeting = meetingService.find(id, requireEngagedTeam != null);
    return meetingMapper.toDetail(meeting, user instanceof Student student ? student : null);
  }

  @GetMapping("me")
  @RolesAllowed({STUDENT})
  @Operation(description = "내 과팅 목록", parameters = @Parameter(required = true, in = ParameterIn.HEADER, name = "Authorization"))
  public CustomSlice<MyMeetingPageListElement> search(Student student,
                                                      @ParameterObject Pageable pageable) {
    Slice<MyMeetingPageListElement> result = meetingService.searchMyMeetingList(student, pageable)
        .map(meetingMapper::toMyMeetingPageListElement);
    return new CustomSlice<>(result);
  }

  @PostMapping
  @RolesAllowed({STUDENT})
  @ResponseStatus(HttpStatus.CREATED)
  @Operation(description = "과팅 생성", parameters = @Parameter(required = true, in = ParameterIn.HEADER, name = "Authorization"))
  public void create(@Valid @RequestBody Create dto, Student member) {
    Meeting entity = meetingMapper.toEntity(dto, member);
    meetingService.save(entity);
  }

  @PostMapping("{id}/requests")
  @RolesAllowed({STUDENT})
  @ResponseStatus(HttpStatus.CREATED)
  @Operation(description = "과팅 신청하기", parameters = @Parameter(required = true, in = ParameterIn.HEADER, name = "Authorization"))
  public void request(@PathVariable UUID id, @Valid @RequestBody MeetingDto.Request dto,
                      Student member) {
    MeetingRequest request = meetingMapper.toRequestEntity(dto, member);
    Meeting meeting = meetingService.find(id);
    meetingService.addRequest(meeting, request);
  }

  @GetMapping("{id}/requests")
  @RolesAllowed({STUDENT, ADMIN})
  @Operation(description = "특정 과팅 신청 목록 조회", parameters = @Parameter(required = true, in = ParameterIn.HEADER, name = "Authorization"))
  public CustomSlice<MeetingRequestDto.Detail> searchRequestsWithMeeting(@PathVariable UUID id, MeetingRequestDto.SearchCondition condition, Member user,
                                                                         Pageable pageable) {
    Meeting meeting = meetingService.find(id);
//    meeting.validOwner(user);
    Slice<MeetingRequestDto.Detail> result = meetingService.searchRequestsWithMeeting(meeting,
        condition, pageable).map(meetingMapper.requestMapper::toDetail);
    return new CustomSlice<>(result);
  }

  @PostMapping("{id}/watchlist-items/add")
  @RolesAllowed({STUDENT})
  @ResponseStatus(HttpStatus.CREATED)
  @Operation(description = "과팅 찜하기", parameters = @Parameter(required = true, in = ParameterIn.HEADER, name = "Authorization"))
  public void addWatchlist(@PathVariable UUID id, Student member) {
    MeetingWatchlistItem watchlistItemEntity = meetingMapper.toWatchlistItemEntity(member);
    Meeting meeting = meetingService.find(id);
    meetingService.addWatchlist(meeting, watchlistItemEntity);
  }

  @DeleteMapping("{id}/watchlist-items/remove")
  @RolesAllowed({STUDENT})
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @Operation(description = "과팅 찜하기 해제", parameters = @Parameter(required = true, in = ParameterIn.HEADER, name = "Authorization"))
  public void takeOutFromWatchlist(@PathVariable UUID id, Student member) {
    MeetingWatchlistItem meetingWatchlistItem = meetingService.findWatchlistItem(id, member);
    meetingWatchlistItem.validOwner(member);
    meetingService.removeWatchlistItem(meetingWatchlistItem);
  }
}

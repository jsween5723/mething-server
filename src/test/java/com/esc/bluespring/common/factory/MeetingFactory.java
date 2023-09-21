package com.esc.bluespring.common.factory;

import com.esc.bluespring.domain.meeting.entity.Meeting;
import java.util.ArrayList;
import java.util.List;

public class MeetingFactory {

  public Meeting testMeeting() {
    return Meeting.builder().build();
  }

  public List<Meeting> testMeetings() {
    List<Meeting> meetings = new ArrayList<>();
    for (int i = 0; i < 12; i++) {
      meetings.add(testMeeting());
    }
    return meetings;
  }

}

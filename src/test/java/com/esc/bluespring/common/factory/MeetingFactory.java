package com.esc.bluespring.common.factory;

import com.esc.bluespring.domain.meeting.entity.MeetingRelation;
import java.util.ArrayList;
import java.util.List;

public class MeetingFactory {

  public MeetingRelation testMeeting() {
    return MeetingRelation.builder().build();
  }

  public List<MeetingRelation> testMeetings() {
    List<MeetingRelation> meetingRelations = new ArrayList<>();
    for (int i = 0; i < 12; i++) {
      meetingRelations.add(testMeeting());
    }
    return meetingRelations;
  }

}

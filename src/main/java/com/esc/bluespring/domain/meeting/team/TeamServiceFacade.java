package com.esc.bluespring.domain.meeting.team;

import com.esc.bluespring.domain.meeting.entity.Meeting;
import com.esc.bluespring.domain.meeting.team.entity.MeetingRequesterTeam;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TeamServiceFacade {
    private final MeetingRequesterTeamService requesterTeamService;

    @Transactional
    public void joinRequest(Meeting meeting, MeetingRequesterTeam requester) {
        MeetingRequesterTeam requesterTeam = requesterTeamService.save(requester);
        requesterTeam.requestTo(meeting);
    }
}

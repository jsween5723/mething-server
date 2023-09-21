package com.esc.bluespring.domain.meeting.team;

import com.esc.bluespring.domain.meeting.team.entity.MeetingRequesterTeam;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
class MeetingRequesterTeamService {
    private final MeetingRequesterTeamRepository repository;

    @Transactional
    public MeetingRequesterTeam save(MeetingRequesterTeam team) {
        return repository.save(team);
    }
}

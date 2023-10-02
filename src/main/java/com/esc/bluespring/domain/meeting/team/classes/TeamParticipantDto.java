package com.esc.bluespring.domain.meeting.team.classes;

import java.util.UUID;

public record TeamParticipantDto() {
    public record MainPageListElement(UUID id, String profileImageUrl, boolean isOwner) {}
}

package com.esc.bluespring.domain.meeting.team.classes;

public record TeamParticipantDto() {
    public record MainPageListElement(Long id, String profileImageUrl, boolean isOwner) {}
}

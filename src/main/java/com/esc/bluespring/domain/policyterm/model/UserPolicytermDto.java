package com.esc.bluespring.domain.policyterm.model;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.UUID;

public record UserPolicytermDto(@Schema(description = "약관 id") UUID id, @Schema(description = "약관 제목", example = "제 1조") String title,
                                @Schema(description = "약관 이동 링크", example = "https://naver.com") String link, @Schema(description = "필수 여부", example = "true") boolean required) {
}

package com.esc.bluespring.common.utils.sms.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageResponse {

  private String requestId;
  private LocalDateTime requestTime;
  private String statusCode;
  private String statusName;

}

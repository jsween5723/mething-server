package com.esc.bluespring.common.utils.sms.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class MessageRequest {

  private String type;
  private String contentType;
  private String countryCode;
  private String from;
  private String content;
  private List<MessageDto> messages;
}

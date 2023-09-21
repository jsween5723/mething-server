package com.esc.bluespring.common.ncp;

import com.esc.bluespring.common.utils.sms.dto.MessageDto;
import com.esc.bluespring.domain.auth.classes.JsonStringfiable;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
class NcpDtoFactory {
  @Value("${naver-cloud.sms.calling-number}")
  private String callNumber;
  private final ObjectMapper objectMapper;
  public SendMessageRequest generateSmsMessageRequest(MessageDto dto) {
   return new SendMessageRequest(dto.content(), List.of(dto));
  }

  private abstract class StringfiableRequest implements JsonStringfiable {
    public String toJson() {
      try {
        return objectMapper.writeValueAsString(this);
      } catch (JsonProcessingException e) {
        throw new RuntimeException(e); //global에서 로깅 처리
      }
    }
  }
  @Getter
  class SendMessageRequest extends StringfiableRequest {
    private final static String MESSAGE_TYPE = "SMS";
    private final static String CONTENT_TYPE = "COMM";
    private final static String COUNTRY_CODE = "82";
    private final String type = MESSAGE_TYPE;
    private final String contentType = CONTENT_TYPE;
    private final String countryCode = COUNTRY_CODE;
    private final String from = callNumber;
    private final String content;
    private final List<MessageDto> messages;
    public SendMessageRequest(String content, List<MessageDto> messages) {
      this.content = content;
      this.messages = messages;
    }
  }

}

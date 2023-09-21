package com.esc.bluespring.common.ncp;

import com.esc.bluespring.common.utils.sms.dto.MessageDto;
import com.esc.bluespring.common.utils.sms.dto.MessageResponse;
import com.esc.bluespring.domain.auth.classes.JsonStringfiable;
import com.esc.bluespring.common.ncp.NcpDtoFactory.SendMessageRequest;
import com.esc.bluespring.common.ncp.NcpHeaderFactory.NcpHeader;
import java.net.URI;
import java.net.URISyntaxException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
class NcpClient {
  static private final String SNES_URL = "https://sens.apigw.ntruss.com";
  private final NcpHeaderFactory headerFactory;
  private final NcpDtoFactory dtoFactory;
  private final RestTemplate template;
  @Value("${naver-cloud.sms.service-id}")
  private String serviceId;

  public void sendMessage(MessageDto message) {
    SendMessageRequest request = dtoFactory.generateSmsMessageRequest(message);
    requestToNcp(HttpMethod.POST, SNES_URL, getSmsSendUrl(), request, MessageResponse.class);
  }

  private <T> ResponseEntity<T> requestToNcp(HttpMethod method, String baseUrl, String path,
      JsonStringfiable body, Class<T> type) {
    NcpHeader ncpHeader = headerFactory.generateHeader(method, path);
    HttpEntity<String> httpBody = new HttpEntity<>(body.toJson(), ncpHeader);
    try {
      return template.exchange(new URI(baseUrl + path), method, httpBody, type);
    } catch (URISyntaxException e) {
      throw new RuntimeException(e); //global에서 로깅 처리
    }
  }

  private String getSmsSendUrl() {
    return "/sms/v2/services/" + serviceId + "/messages";
  }
}

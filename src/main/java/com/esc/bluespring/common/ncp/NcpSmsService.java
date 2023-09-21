package com.esc.bluespring.common.ncp;

import com.esc.bluespring.common.utils.sms.dto.MessageDto;
import com.esc.bluespring.common.utils.sms.service.SmsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class NcpSmsService implements SmsService {
  private final NcpClient client;
  @Override
  public void sendSMS(MessageDto dto) {
    client.sendMessage(dto);
  }
}

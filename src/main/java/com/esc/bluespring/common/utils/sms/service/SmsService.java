package com.esc.bluespring.common.utils.sms.service;

import com.esc.bluespring.common.utils.sms.dto.MessageDto;

public interface SmsService {
  void sendSMS(MessageDto dto);
}

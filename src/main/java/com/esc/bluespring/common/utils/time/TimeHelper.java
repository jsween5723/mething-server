package com.esc.bluespring.common.utils.time;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

public class TimeHelper {

  // MVP 기준, 미띵은 KST 기준으로 동작
  private static final ZoneId ZONE_ID = ZoneId.of("Asia/Seoul");

  public static long convertToUnix(LocalDateTime dateTime) {
    LocalDateTime zonedDateTime = convertToTimezone(dateTime);
    return zonedDateTime.toEpochSecond(ZONE_ID.getRules().getOffset(Instant.now()));
  }

  public static LocalDateTime convertToTimezone(LocalDateTime dateTime) {
    ZonedDateTime timezoneDateTime = dateTime.atZone(ZoneOffset.UTC).withZoneSameInstant(ZONE_ID);
    return timezoneDateTime.toLocalDateTime();
  }

}

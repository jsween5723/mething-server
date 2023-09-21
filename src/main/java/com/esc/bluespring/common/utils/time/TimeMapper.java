package com.esc.bluespring.common.utils.time;

import java.time.LocalDateTime;
import java.time.ZoneId;
import org.springframework.stereotype.Component;

@Component
public class TimeMapper {
    public long toEpochSecond(LocalDateTime localDateTime) {
        return localDateTime.atZone(ZoneId.of("Asia/Seoul")).toEpochSecond();
    }
}

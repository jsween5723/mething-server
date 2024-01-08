package com.esc.bluespring.common.utils.time;

import java.time.LocalDateTime;
import java.time.ZoneId;
import org.mapstruct.Mapper;

@Mapper
public interface TimeMapper {

    default long toEpochSecond(LocalDateTime localDateTime) {
        return localDateTime.atZone(ZoneId.of("Asia/Seoul")).toInstant().toEpochMilli();
    }
}

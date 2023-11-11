package com.esc.bluespring.domain.file;

import com.esc.bluespring.domain.file.entity.Image;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface FileMapper {
    FileMapper INSTANCE = Mappers.getMapper(FileMapper.class);
    Image toImage(String url);
}

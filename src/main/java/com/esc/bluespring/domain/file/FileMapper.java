package com.esc.bluespring.domain.file;

import com.esc.bluespring.domain.file.entity.Image;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface FileMapper {
    FileMapper INSTANCE = Mappers.getMapper(FileMapper.class);
    @Mapping(target = "url", source = "url")
    Image toImage(String url);
}

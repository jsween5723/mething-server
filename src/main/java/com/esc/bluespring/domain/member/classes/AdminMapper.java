package com.esc.bluespring.domain.member.classes;

import com.esc.bluespring.domain.member.classes.MemberDto.AdminJoin;
import com.esc.bluespring.domain.member.entity.Admin;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AdminMapper {
    AdminMapper INSTANCE = Mappers.getMapper(AdminMapper.class);

    Admin toEntity(AdminJoin dto);

    @Mapping(target = "id", source = "id")
    Admin toEntity(Long id);
}

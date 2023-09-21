package com.esc.bluespring.domain.member.classes;

import com.esc.bluespring.domain.member.classes.MemberDto.AdminJoin;
import com.esc.bluespring.domain.member.entity.Admin;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants.ComponentModel;

@Mapper(injectionStrategy = InjectionStrategy.CONSTRUCTOR, componentModel = ComponentModel.SPRING)
public interface AdminMapper {

    Admin toEntity(AdminJoin dto);

    @Mapping(target = "id", source = "id")
    Admin toEntity(Long id);
}

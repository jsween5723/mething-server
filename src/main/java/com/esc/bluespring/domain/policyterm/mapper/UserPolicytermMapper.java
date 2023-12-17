package com.esc.bluespring.domain.policyterm.mapper;


import com.esc.bluespring.domain.policyterm.entity.UserPolicyterm;
import com.esc.bluespring.domain.policyterm.model.UserPolicytermDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserPolicytermMapper {
    UserPolicytermMapper INSTANCE = Mappers.getMapper(UserPolicytermMapper.class);
    UserPolicytermDto toDto(UserPolicyterm buyerPolicyterm);
}

package com.esc.bluespring.domain.member.classes;

import com.esc.bluespring.domain.member.entity.StudentInterest;
import com.esc.bluespring.domain.member.entity.profile.Interest;
import java.util.Set;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface InterestMapper {
    InterestMapper INSTANCE = Mappers.getMapper(InterestMapper.class);
    Interest toEntity(String name);
    StudentInterest toMiddleEntity(Interest interest);
    Set<StudentInterest> toMiddles(Set<String> interests);
    default String toString(StudentInterest studentInterest) {
        return studentInterest.getInterest().getName();
    };
}

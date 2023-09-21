package com.esc.bluespring.domain.member.classes;

import com.esc.bluespring.common.utils.file.S3Service;
import com.esc.bluespring.domain.meeting.mapper.TeamParticipantMapper;
import com.esc.bluespring.domain.member.classes.MemberDto.Detail;
import com.esc.bluespring.domain.member.classes.MemberDto.Join;
import com.esc.bluespring.domain.member.entity.SchoolInformation;
import com.esc.bluespring.domain.member.entity.Student;
import com.esc.bluespring.domain.university.major.classes.MajorMapper;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants.ComponentModel;

@Mapper(uses = {MajorMapper.class, S3Service.class,
    TeamParticipantMapper.class}, injectionStrategy = InjectionStrategy.CONSTRUCTOR, componentModel = ComponentModel.SPRING)
public interface StudentMapper {

    @Mapping(target = "profileImage", source = "profileImage")
    @Mapping(target = "schoolInformation", source = "dto")
    Student toEntity(Join dto);

    @Mapping(target = "id", source = "id")
    Student toEntity(Long id);

    @Mapping(target = "profileImageUrl", source = "member.profileImage.url")
    Detail toDetail(Student member);

    SchoolInformation toSchoolInformation(Join dto);
}

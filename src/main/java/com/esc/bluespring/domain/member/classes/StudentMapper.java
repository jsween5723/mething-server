package com.esc.bluespring.domain.member.classes;

import com.esc.bluespring.domain.file.entity.Image;
import com.esc.bluespring.domain.member.classes.MemberDto.Detail;
import com.esc.bluespring.domain.member.classes.MemberDto.Join;
import com.esc.bluespring.domain.member.entity.SchoolInformation;
import com.esc.bluespring.domain.member.entity.Student;
import com.esc.bluespring.domain.university.major.classes.MajorMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {MajorMapper.class})
public interface StudentMapper {
    StudentMapper INSTANCE = Mappers.getMapper(StudentMapper.class);
    @Mapping(target = "profileImage", source = "profileImage")
    @Mapping(target = "schoolInformation", expression = "java(toSchoolInformation(dto, profileImage, studentCertificationImage))")
    @Mapping(target = "id", ignore = true)
    Student toEntity(Join dto, Image profileImage, Image studentCertificationImage);

    @Mapping(target = "id", source = "id")
    Student toEntity(Long id);

    @Mapping(target = "profileImageUrl", source = "member.profileImage.url")
    Detail toDetail(Student member);

    SchoolInformation toSchoolInformation(Join dto, Image profileImage, Image studentCertificationImage);
}

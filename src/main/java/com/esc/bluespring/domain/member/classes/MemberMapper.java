package com.esc.bluespring.domain.member.classes;

import com.esc.bluespring.common.utils.time.TimeMapper;
import com.esc.bluespring.domain.file.entity.Image;
import com.esc.bluespring.domain.member.classes.MemberDto.AdminJoin;
import com.esc.bluespring.domain.member.classes.MemberDto.Join;
import com.esc.bluespring.domain.member.classes.MemberDto.Patch;
import com.esc.bluespring.domain.member.entity.Admin;
import com.esc.bluespring.domain.member.entity.SchoolInformation;
import com.esc.bluespring.domain.member.entity.Student;
import com.esc.bluespring.domain.member.student.classes.StudentDto.Detail;
import com.esc.bluespring.domain.member.student.classes.StudentDto.TeamListElement;
import com.esc.bluespring.domain.university.major.classes.MajorMapper;
import java.util.UUID;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {MajorMapper.class, TimeMapper.class})
public interface MemberMapper {
    MemberMapper INSTANCE = Mappers.getMapper(MemberMapper.class);
    @Mapping(target = "profileImage", source = "profileImage")
    @Mapping(target = "schoolInformation", expression = "java(toSchoolInformation(dto, studentCertificationImage))")
    @Mapping(target = "id", ignore = true)
    Student toEntity(Join dto, Image profileImage, Image studentCertificationImage);
    Admin toEntity(AdminJoin dto);
    @Mapping(target = "profileImage", source = "profileImage")
    @Mapping(target = "schoolInformation", expression = "java(toSchoolInformation(dto, studentCertificationImage))")
    @Mapping(target = "id", ignore = true)
    Student toEntity(Patch dto, Image profileImage, Image studentCertificationImage);
    TeamListElement toSchoolInformationListElement(Student student);
    @Mapping(target = "id", ignore = true)
    Admin toEntity(Patch dto);
    @Mapping(target = "id", source = "id")
    Student toEntity(UUID id);

    Detail toDetail(Student member);
    @Mapping(target = "major", source = "dto.majorId")
    @Mapping(target = "studentCertificationImage", source = " studentCertificationImage")
    SchoolInformation toSchoolInformation(Join dto, Image studentCertificationImage);
    SchoolInformation toSchoolInformation(Patch dto, Image studentCertificationImage);
}

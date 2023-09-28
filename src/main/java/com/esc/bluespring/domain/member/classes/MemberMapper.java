package com.esc.bluespring.domain.member.classes;

import com.esc.bluespring.domain.file.entity.Image;
import com.esc.bluespring.domain.member.classes.MemberDto.AdminJoin;
import com.esc.bluespring.domain.member.classes.MemberDto.Detail;
import com.esc.bluespring.domain.member.classes.MemberDto.Join;
import com.esc.bluespring.domain.member.classes.MemberDto.Patch;
import com.esc.bluespring.domain.member.entity.Admin;
import com.esc.bluespring.domain.member.entity.SchoolInformation;
import com.esc.bluespring.domain.member.entity.Student;
import com.esc.bluespring.domain.member.student.classes.StudentDto.SchoolInformationListElement;
import com.esc.bluespring.domain.university.major.classes.MajorMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {MajorMapper.class})
public interface MemberMapper {
    MemberMapper INSTANCE = Mappers.getMapper(MemberMapper.class);
    @Mapping(target = "profileImage", source = "profileImage")
    @Mapping(target = "schoolInformation", expression = "java(toSchoolInformation(dto, profileImage, studentCertificationImage))")
    @Mapping(target = "id", ignore = true)
    Student toEntity(Join dto, Image profileImage, Image studentCertificationImage);

    Admin toEntity(AdminJoin dto);

    @Mapping(target = "profileImage", source = "profileImage")
    @Mapping(target = "schoolInformation", expression = "java(toSchoolInformation(dto, profileImage, studentCertificationImage))")
    @Mapping(target = "id", ignore = true)
    Student toEntity(Patch dto, Image profileImage, Image studentCertificationImage);


    @Mapping(target = "studentCertificationImageUrl", source = "schoolInformation.studentCertificationImage.url")
    @Mapping(target = "name", source = "schoolInformation.name")
    @Mapping(target = "major", source = "schoolInformation.major")
    @Mapping(target = "isCertificated", source = "schoolInformation.certificated")
    SchoolInformationListElement toSchoolInformationListElement(Student student);

    @Mapping(target = "id", ignore = true)
    Admin toEntity(Patch dto);
    @Mapping(target = "id", source = "id")
    Student toEntity(Long id);

    @Mapping(target = "profileImageUrl", source = "member.profileImage.url")
    Detail toDetail(Student member);

    SchoolInformation toSchoolInformation(Join dto, Image profileImage, Image studentCertificationImage);
    SchoolInformation toSchoolInformation(Patch dto, Image profileImage, Image studentCertificationImage);
}

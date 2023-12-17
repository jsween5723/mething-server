package com.esc.bluespring.domain.member.classes;


import com.esc.bluespring.common.utils.time.TimeMapper;
import com.esc.bluespring.domain.file.FileMapper;
import com.esc.bluespring.domain.member.classes.MemberDto.JoinRequest;
import com.esc.bluespring.domain.member.classes.MemberDto.ProfileDto;
import com.esc.bluespring.domain.member.classes.MemberDto.SchoolInformationDto;
import com.esc.bluespring.domain.member.entity.SchoolInformation;
import com.esc.bluespring.domain.member.entity.Student;
import com.esc.bluespring.domain.member.entity.StudentProfile;
import com.esc.bluespring.domain.member.student.classes.StudentDto.DetailResponse;
import com.esc.bluespring.domain.member.student.classes.StudentDto.ListElement;
import com.esc.bluespring.domain.policyterm.entity.UserPolicyterm;
import com.esc.bluespring.domain.university.major.classes.MajorMapper;
import java.util.Set;
import java.util.UUID;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {MajorMapper.class, TimeMapper.class, InterestMapper.class, FileMapper.class})
public interface MemberMapper {

    MemberMapper INSTANCE = Mappers.getMapper(MemberMapper.class);

    @Mapping(target = "id", ignore = true)
    Student toEntity(JoinRequest dto);
    Set<UserPolicyterm> toPolicytermEntity(Set<UUID> policyterms);
    UserPolicyterm toPolicytermEntity(UUID id);
    ListElement toSchoolInformationListElement(Student student);
    @Mapping(target = "id", source = "id")
    Student toEntity(UUID id);
    StudentProfile toEntity(ProfileDto dto);
    SchoolInformation toEntity(SchoolInformationDto dto);
    DetailResponse toDetail(Student student);
}

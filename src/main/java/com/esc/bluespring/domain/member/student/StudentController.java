package com.esc.bluespring.domain.member.student;

import static com.esc.bluespring.domain.member.entity.Member.ADMIN;
import static com.esc.bluespring.domain.member.entity.Member.STUDENT;

import com.esc.bluespring.common.CustomSlice;
import com.esc.bluespring.domain.member.classes.MemberMapper;
import com.esc.bluespring.domain.member.entity.Student;
import com.esc.bluespring.domain.member.student.classes.StudentDto;
import com.esc.bluespring.domain.member.student.classes.StudentDto.SchoolInformationListElement;
import com.esc.bluespring.domain.member.student.classes.StudentDto.SearchCondition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/students")
public class StudentController {

    private final MemberMapper mapper = MemberMapper.INSTANCE;
    private final StudentService studentService;

    @GetMapping("school-informations")
    @RolesAllowed({ADMIN})
    @Operation(description = "학생증 인증을 위한 어드민용 학생정보 조회 API입니다.")
    public CustomSlice<SchoolInformationListElement> search(
        @ParameterObject SearchCondition condition, @ParameterObject Pageable pageable) {
        Slice<SchoolInformationListElement> result = studentService.searchForAdmin(condition,
            pageable).map(mapper::toSchoolInformationListElement);
        return new CustomSlice<>(result);
    }

    @PatchMapping("{id}")
    @RolesAllowed({ADMIN})
    @Operation(description = " 관리자용 학생증 인증 API입니다.")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void changeCertificationStatus(@PathVariable String id,
        @Valid @RequestBody StudentDto.ChangeCertificationState dto) {
        Student student = studentService.find(id);
        studentService.changeCertificationState(student, dto.certificationState());
    }

    @GetMapping("authenticated")
    @RolesAllowed({STUDENT})
    @Operation(description = "학생 인증여부 확인 API입니다.")
    public Boolean isAuthenticated(Student student) {
        return student.isCertificated();
    }
}

package com.esc.bluespring.domain.member.student;

import static com.esc.bluespring.domain.member.entity.Member.ADMIN;
import static com.esc.bluespring.domain.member.entity.Member.STUDENT;

import com.esc.bluespring.common.BaseResponse;
import com.esc.bluespring.common.CustomSlice;
import com.esc.bluespring.domain.member.classes.MemberMapper;
import com.esc.bluespring.domain.member.entity.Student;
import com.esc.bluespring.domain.member.student.classes.StudentDto;
import com.esc.bluespring.domain.member.student.classes.StudentDto.AdminStudentSearchCondition;
import com.esc.bluespring.domain.member.student.classes.StudentDto.ChangeCertificationImage;
import com.esc.bluespring.domain.member.student.classes.StudentDto.DetailResponse;
import com.esc.bluespring.domain.member.student.classes.StudentDto.ListElement;
import com.esc.bluespring.domain.member.student.classes.StudentDto.StudentSearchCondition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
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
    @Operation(description = "학생증 인증을 위한 어드민용 학생정보 조회 API입니다.", parameters = @Parameter(required = true, in = ParameterIn.HEADER, name = "Authorization"))
    public BaseResponse<CustomSlice<ListElement>> search(
        @ParameterObject AdminStudentSearchCondition condition,
        @ParameterObject Pageable pageable) {
        Slice<ListElement> result = studentService.searchForAdmin(condition, pageable)
            .map(mapper::toSchoolInformationListElement);
        return new BaseResponse<>(new CustomSlice<>(result));
    }

    @PatchMapping("{id}")
    @RolesAllowed({ADMIN})
    @Operation(description = " 관리자용 학생증 인증 API입니다.", parameters = @Parameter(required = true, in = ParameterIn.HEADER, name = "Authorization"))
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public BaseResponse<Boolean> changeCertificationStatus(@PathVariable String id,
                                                           @Valid @RequestBody StudentDto.ChangeCertificationState dto) {
        Student student = studentService.find(id);
        studentService.changeCertificationState(student, dto.certificationState());
        return new BaseResponse<>(true);
    }

    @GetMapping("authenticated")
    @RolesAllowed({STUDENT})
    @Operation(description = "학생 인증여부 확인 API입니다.", parameters = @Parameter(required = true, in = ParameterIn.HEADER, name = "Authorization"))
    public BaseResponse<Boolean> isAuthenticated(Student student) {
        return new BaseResponse<>(student.isCertificated());
    }

    @GetMapping
    @RolesAllowed({STUDENT})
    @Operation(description = "닉네임으로 검색합니다.", summary = "학생 검색 API입니다.", parameters = @Parameter(required = true, in = ParameterIn.HEADER, name = "Authorization"))
    public BaseResponse<CustomSlice<ListElement>> search(
        @ParameterObject StudentSearchCondition condition, @ParameterObject Pageable pageable) {
        return new BaseResponse<>(new CustomSlice<>(studentService.search(condition, pageable)
            .map(mapper::toSchoolInformationListElement)));
    }

    @GetMapping("{id}")
    @RolesAllowed({STUDENT})
    @Operation(description = "상세정보를 조회합니다.", summary = "학생의 상세정보를 조회합니다.", parameters = @Parameter(required = true, in = ParameterIn.HEADER, name = "Authorization"))
    public BaseResponse<DetailResponse> getDetail(@PathVariable UUID id) {
        return new BaseResponse<>(mapper.toDetail(studentService.getDetail(id)));
    }

    @GetMapping("me")
    @RolesAllowed({STUDENT})
    @Operation(description = "상세정보를 조회합니다.", summary = "로그인된 학생의 상세정보를 조회합니다.", parameters = @Parameter(required = true, in = ParameterIn.HEADER, name = "Authorization"))
    public BaseResponse<DetailResponse> getMe(Student student) {
        return new BaseResponse<>(mapper.toDetail(studentService.getDetail(student.getId())));
    }

    @GetMapping("duplicate-email")
    public BaseResponse<Boolean> checkEmail(@Parameter String email) {
        studentService.validEmail(email);
        return new BaseResponse<>(true);
    }

    @GetMapping("duplicate-nickname")
    public BaseResponse<Boolean> checkNickname(@Parameter String nickname) {
        studentService.validNickname(nickname);
        return new BaseResponse<>(true);
    }

    @PatchMapping("certification")
    @Operation(description = "학생증 사진을 변경합니다.", summary = "학생증 사진을 변경합니다.", parameters = @Parameter(required = true, in = ParameterIn.HEADER, name = "Authorization"))
    public BaseResponse<Boolean> changeCertificationImage(
        @RequestBody @Valid ChangeCertificationImage dto, Student student) {
        studentService.changeCertificationImage(student, dto.certificationImageUrl());
        return new BaseResponse<>(true);
    }
}

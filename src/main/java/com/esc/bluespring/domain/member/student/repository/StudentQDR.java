package com.esc.bluespring.domain.member.student.repository;

import com.esc.bluespring.domain.member.entity.Student;
import com.esc.bluespring.domain.member.student.classes.StudentDto.AdminStudentSearchCondition;
import com.esc.bluespring.domain.member.student.classes.StudentDto.StudentSearchCondition;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface StudentQDR {
    Slice<Student> searchForAdmin(AdminStudentSearchCondition condition, Pageable pageable);
    Slice<Student> searchByNickname(StudentSearchCondition nickname, Pageable pageable);
}

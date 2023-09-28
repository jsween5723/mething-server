package com.esc.bluespring.domain.member.student.repository;

import com.esc.bluespring.domain.member.entity.Student;
import com.esc.bluespring.domain.member.student.classes.StudentDto.SearchCondition;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface StudentQDR {
    Slice<Student> searchForAdmin(SearchCondition condition, Pageable pageable);
}

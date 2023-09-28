package com.esc.bluespring.domain.member.student;

import com.esc.bluespring.domain.member.entity.Student;
import com.esc.bluespring.domain.member.exception.MemberException.DuplicateNicknameException;
import com.esc.bluespring.domain.member.exception.MemberException.DuplicateSchoolEmailException;
import com.esc.bluespring.domain.member.exception.MemberException.MemberNotFoundException;
import com.esc.bluespring.domain.member.student.classes.StudentDto.SearchCondition;
import com.esc.bluespring.domain.member.student.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository repository;

    @Transactional
    public Student join(Student entity) {
        validForm(entity);
        return repository.save(entity);
    }

    @Transactional(readOnly = true)
    public Slice<Student> searchForAdmin(SearchCondition condition, Pageable pageable) {
        return repository.searchForAdmin(condition, pageable);
    }

    private void validForm(Student entity) {
        if (repository.existsByEmail(entity.getEmail())) {
            throw new DuplicateSchoolEmailException();
        }
        if (repository.existsByNickname(entity.getNickname())) {
            throw new DuplicateNicknameException();
        }
    }

    @Transactional(readOnly = true)
    public Student find(String email) {
        return repository.findByEmail(email).orElseThrow(MemberNotFoundException::new);
    }

    @Transactional(readOnly = true)
    public Student find(Long id) {
        return repository.findById(id).orElseThrow(MemberNotFoundException::new);
    }

    @Transactional
    public void changeCertificationState(Student target, boolean state) {
        target.changeCertificationState(state);
    }
}

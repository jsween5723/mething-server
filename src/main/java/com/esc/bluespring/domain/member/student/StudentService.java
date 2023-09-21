package com.esc.bluespring.domain.member.student;

import com.esc.bluespring.domain.member.entity.Student;
import com.esc.bluespring.domain.member.exception.MemberException.DuplicateNicknameException;
import com.esc.bluespring.domain.member.exception.MemberException.DuplicateSchoolEmailException;
import lombok.RequiredArgsConstructor;
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

    private void validForm(Student entity) {
        if (repository.existsByEmail(entity.getEmail())) {
            throw new DuplicateSchoolEmailException();
        }
        if (repository.existsByNickname(entity.getNickname())) {
            throw new DuplicateNicknameException();
        }
    }
}

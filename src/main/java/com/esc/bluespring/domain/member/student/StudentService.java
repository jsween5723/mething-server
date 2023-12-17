package com.esc.bluespring.domain.member.student;

import com.esc.bluespring.domain.file.FileService;
import com.esc.bluespring.domain.member.entity.Student;
import com.esc.bluespring.domain.member.entity.UserPolicytermAgreement;
import com.esc.bluespring.domain.member.entity.profile.CertificateStatus;
import com.esc.bluespring.domain.member.exception.MemberException.DuplicateNicknameException;
import com.esc.bluespring.domain.member.exception.MemberException.DuplicateSchoolEmailException;
import com.esc.bluespring.domain.member.exception.MemberException.MemberNotFoundException;
import com.esc.bluespring.domain.member.exception.MemberException.RequiredPolicytermIgnoredException;
import com.esc.bluespring.domain.member.student.classes.StudentDto.AdminStudentSearchCondition;
import com.esc.bluespring.domain.member.student.classes.StudentDto.StudentSearchCondition;
import com.esc.bluespring.domain.member.student.repository.StudentRepository;
import com.esc.bluespring.domain.policyterm.UserPolicytermService;
import com.esc.bluespring.domain.policyterm.entity.UserPolicyterm;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository repository;
    private final FileService fileService;
    private final UserPolicytermService policytermService;

    @Transactional
    public Student join(Student entity) {
        validForm(entity);
        return repository.save(entity);
    }

    @Transactional(readOnly = true)
    public Slice<Student> search(StudentSearchCondition condition, Pageable pageable) {
        return repository.search(condition, pageable);
    }

    @Transactional(readOnly = true)
    public Slice<Student> searchForAdmin(AdminStudentSearchCondition condition, Pageable pageable) {
        return repository.searchForAdmin(condition, pageable);
    }

    @Transactional(readOnly = true)
    public Student getDetail(UUID uuid) {
        return repository.findDetail(uuid);
    }

    private void validForm(Student entity) {
        validEmail(entity.getEmail());
        validNickname(entity.getNickname());
        validPolicyterm(entity.getPolicytermAgreements());
    }

    private void validPolicyterm(Set<UserPolicytermAgreement> policytermAgreements) {
        Set<UserPolicyterm> policyterms = policytermAgreements.stream()
            .map(UserPolicytermAgreement::getPolicyterm).collect(Collectors.toSet());
        if (!policytermService.isContainsRequiredPolicyterms(policyterms)){
            throw new RequiredPolicytermIgnoredException();
        }
    }

    public void validEmail(String email) {
        if (repository.existsByEmail(email)) {
            throw new DuplicateSchoolEmailException();
        }
    }

    public void validNickname(String nickname) {
        if (repository.existsByNickname(nickname)) {
            throw new DuplicateNicknameException();
        }
    }

    @Transactional(readOnly = true)
    public Student find(String email) {
        return repository.findByEmail(email).orElseThrow(MemberNotFoundException::new);
    }

    @Transactional(readOnly = true)
    public Student find(UUID id) {
        return repository.findById(id).orElseThrow(MemberNotFoundException::new);
    }

    @Transactional
    public void changeCertificationState(Student target, CertificateStatus state) {
        target.changeCertificationState(state);
    }

    @Transactional
    public void changeCertificationImage(Student student, String imageUrl) {
        student.reassignCertificationImage(fileService.findByUrl(imageUrl));
        repository.save(student);
    }
}

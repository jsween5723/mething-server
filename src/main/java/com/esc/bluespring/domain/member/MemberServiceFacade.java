package com.esc.bluespring.domain.member;

import com.esc.bluespring.domain.auth.exception.AuthException.LoginFailedException;
import com.esc.bluespring.domain.auth.service.emailCode.EmailAuthenticationService;
import com.esc.bluespring.domain.member.classes.MemberDto.Login;
import com.esc.bluespring.domain.member.entity.Member;
import com.esc.bluespring.domain.member.entity.Student;
import com.esc.bluespring.domain.member.exception.MemberException.DuplicateEmailException;
import com.esc.bluespring.domain.member.exception.MemberException.MemberNotFoundException;
import com.esc.bluespring.domain.member.student.StudentService;
import java.util.UUID;
import javax.security.auth.login.LoginException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberServiceFacade implements UserDetailsService {

    private final EmailAuthenticationService emailAuthenticationService;
    private final MemberRepository repository;
    private final StudentService studentService;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public Member join(Member member) {
        validForm(member);
        String encoded = passwordEncoder.encode(member.getPassword());
        member.changePassword(encoded);
        emailAuthenticationService.isAuthenticated(member.getEmail());
        if (member instanceof Student student) {
            return studentService.join(student);
        }
        return repository.save(member);
    }

    @Transactional(readOnly = true)
    public Member find(UUID id) {
        Member member = repository.findById(id).orElseThrow(MemberNotFoundException::new);
        if (member instanceof Student) {
            return studentService.find(id);
        }
        return member;
    }

    private void validForm(Member entity) {
        if (repository.existsByEmail(entity.getEmail())) {
            throw new DuplicateEmailException();
        }
    }

    @Transactional
    public void requestFriendship(Member user, Member target, String message) {
        if (user instanceof Student studentUser && target instanceof Student targetStudent) {
            studentUser.friendshipRequestTo(targetStudent, message);
        }
    }

    @Transactional
    public void resign(Member member) {
        repository.delete(member);
    }

    @Override
    @Transactional(readOnly = true)
    public Member loadUserByUsername(String username) {
        return repository.findByEmail(username).orElseThrow(MemberNotFoundException::new);
    }

    @Transactional(readOnly = true)
    public Member login(Login dto) {
        try {
            Member userDetails = loadUserByUsername(dto.email());
            if (!passwordEncoder.matches(dto.password(), userDetails.getPassword())) {
                throw new LoginException();
            }
            return userDetails;
        } catch (Exception e) {
            throw new LoginFailedException();
        }
    }

    @Transactional
    public void patch(Member source, Member target) {
        if (source.getEmail() != null) {
            emailAuthenticationService.isAuthenticated(source.getEmail());
        }
        if (source.getPassword() != null) {
            emailAuthenticationService.isAuthenticated(source.getEmail());
            String encoded = passwordEncoder.encode(source.getPassword());
            source.changePassword(encoded);
        }
        target.patch(source);
    }
}

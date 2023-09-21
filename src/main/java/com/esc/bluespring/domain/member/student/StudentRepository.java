package com.esc.bluespring.domain.member.student;

import com.esc.bluespring.domain.member.entity.Student;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, Long> {
    Optional<Student> findByEmail(String email);
    boolean existsByEmail(String email);

    boolean existsByNickname(String nickname);
}
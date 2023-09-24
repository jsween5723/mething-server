package com.esc.bluespring.domain.member.student;

import com.esc.bluespring.domain.member.entity.Student;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface StudentRepository extends JpaRepository<Student, Long> {

    Optional<Student> findByEmail(String email);

    boolean existsByEmail(String email);

    boolean existsByNickname(String nickname);

    @Override
    @Query("select s from Student s "
        + "left join fetch s.schoolInformation.major "
        + "left join fetch s.schoolInformation.major.university "
        + "where s.id=:id")
    Optional<Student> findById(Long id);
}
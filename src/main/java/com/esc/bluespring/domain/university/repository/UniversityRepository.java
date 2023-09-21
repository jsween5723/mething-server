package com.esc.bluespring.domain.university.repository;

import com.esc.bluespring.domain.university.entity.University;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UniversityRepository extends JpaRepository<University, Long>, UniversityQDR {
}

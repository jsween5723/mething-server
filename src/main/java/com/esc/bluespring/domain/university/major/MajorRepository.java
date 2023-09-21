package com.esc.bluespring.domain.university.major;

import com.esc.bluespring.domain.university.major.entity.Major;
import com.esc.bluespring.domain.university.major.repository.MajorQDR;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MajorRepository extends JpaRepository<Major, Long>, MajorQDR {
}

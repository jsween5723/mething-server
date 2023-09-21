package com.esc.bluespring.domain.university.repository;

import com.esc.bluespring.domain.university.classes.UniversityDto.SearchCondition;
import com.esc.bluespring.domain.university.entity.University;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface UniversityQDR {
    Slice<University> search(SearchCondition condition, Pageable pageable);
}

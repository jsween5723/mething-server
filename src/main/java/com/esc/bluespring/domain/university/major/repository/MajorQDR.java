package com.esc.bluespring.domain.university.major.repository;

import com.esc.bluespring.domain.university.major.classes.MajorDto.SearchCondition;
import com.esc.bluespring.domain.university.major.entity.Major;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface MajorQDR {
    Slice<Major> search(SearchCondition condition, Pageable pageable);
}

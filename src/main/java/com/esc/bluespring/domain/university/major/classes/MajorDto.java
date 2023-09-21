package com.esc.bluespring.domain.university.major.classes;

import com.esc.bluespring.domain.university.classes.UniversityDto;

public record MajorDto() {
    public record SearchCondition(String name, Long universityId) {}
    public record SearchListElement(Long id, String name, UniversityDto.SearchListElement university) {}
}

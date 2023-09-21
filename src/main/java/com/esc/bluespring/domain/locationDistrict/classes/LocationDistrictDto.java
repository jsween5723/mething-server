package com.esc.bluespring.domain.locationDistrict.classes;

public record LocationDistrictDto() {
    public record MainPageListElement(Long id, String name) {}

    public record SearchCondition(String name, Long parentId) {

    }

    public record SearchListElement(Long id, String name, NoParent parent) {}
    public record NoParent(Long id, String name) {}
}

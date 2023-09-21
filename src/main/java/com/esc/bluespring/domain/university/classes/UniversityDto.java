package com.esc.bluespring.domain.university.classes;

import com.esc.bluespring.domain.locationDistrict.classes.LocationDistrictDto;

public record UniversityDto() {

    public record MainPageListElement(Long id, String name, String campus, String type,
                                      LocationDistrictDto.MainPageListElement locationDistrict) {

    }

    public record SearchCondition(String name) {

    }

    public record SearchListElement(Long id, String name, String campus, String type,
                                    LocationDistrictDto.SearchListElement locationDistrict) {

    }
}

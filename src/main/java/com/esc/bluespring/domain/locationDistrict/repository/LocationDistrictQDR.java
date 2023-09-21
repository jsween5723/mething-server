package com.esc.bluespring.domain.locationDistrict.repository;

import com.esc.bluespring.domain.locationDistrict.classes.LocationDistrictDto.SearchCondition;
import com.esc.bluespring.domain.locationDistrict.entity.LocationDistrict;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface LocationDistrictQDR {
    Slice<LocationDistrict> search(SearchCondition condition, Pageable pageable);
}

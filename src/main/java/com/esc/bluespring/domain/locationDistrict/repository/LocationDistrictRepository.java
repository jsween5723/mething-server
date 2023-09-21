package com.esc.bluespring.domain.locationDistrict.repository;

import com.esc.bluespring.domain.locationDistrict.entity.LocationDistrict;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocationDistrictRepository extends JpaRepository<LocationDistrict, Long>,
    LocationDistrictQDR {

}

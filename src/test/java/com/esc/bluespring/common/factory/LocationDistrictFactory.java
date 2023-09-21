package com.esc.bluespring.common.factory;

import com.esc.bluespring.domain.locationDistrict.entity.LocationDistrict;

public class LocationDistrictFactory {
  public LocationDistrict makeLocationDistrict(Long id, String name, LocationDistrict parent) {
    return LocationDistrict.builder()
        .id(id)
        .name(name)
        .parent(parent)
        .build();
  }

  public LocationDistrict makeLocationDistrict(String name, LocationDistrict parent) {
    return makeLocationDistrict(null, name, parent);
  }

  public LocationDistrict makeLocationDistrict(String name) {
    return makeLocationDistrict(name, null);
  }

  public LocationDistrict makeLocationDistrict(long l) {
    return makeLocationDistrict(l, null, null);
  }
}

package com.esc.bluespring.domain.locationDistrict.classes;

import com.esc.bluespring.domain.locationDistrict.entity.LocationDistrict;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Component;

@Component
public class LocationDistrictIndex {
  private final Map<Long, LocationDistrict> index = new ConcurrentHashMap<>();
  public boolean isNotIndexed(Long id) {
    return !index.containsKey(id);
  }
  public LocationDistrict get(Long id) {
    return index.get(id);
  }
  public void put(Long id, LocationDistrict value) {
    index.put(id, value);
  }
}

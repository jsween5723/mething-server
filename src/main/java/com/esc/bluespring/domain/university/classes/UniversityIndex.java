package com.esc.bluespring.domain.university.classes;

import com.esc.bluespring.domain.university.entity.University;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Component;

@Component
public class UniversityIndex {
  private final Map<University, University> index = new ConcurrentHashMap<>();
  public boolean isNotIndexed(University id) {
    return !index.containsKey(id);
  }
  public University get(University id) {
    return index.get(id);
  }
  public void put(University id, University value) {
    index.put(id, value);
  }
}

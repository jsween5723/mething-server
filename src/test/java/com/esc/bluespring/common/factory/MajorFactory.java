package com.esc.bluespring.common.factory;

import com.esc.bluespring.domain.university.major.entity.Major;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.IntStream;

public class MajorFactory {

  public Major testMajor(Long id, String name, com.esc.bluespring.domain.university.entity.University university) {
    return Major.builder()
        .id(id)
        .name(name)
        .university(university)
        .createdAt(LocalDateTime.now())
        .updatedAt(LocalDateTime.now())
        .build();
  }

  public Major testMajor(String name, com.esc.bluespring.domain.university.entity.University university) {
    return Major.builder()
        .name(name)
        .university(university)
        .build();
  }

  public Major testMajor(String name) {
    return Major.builder().name(name).build();
  }

  public List<Major> testMajors(int amount, com.esc.bluespring.domain.university.entity.University university) {
    return IntStream.rangeClosed(1, amount)
        .mapToObj(i -> "학과 " + i)
        .map(i -> testMajor(i, university))
        .toList();
  }

}

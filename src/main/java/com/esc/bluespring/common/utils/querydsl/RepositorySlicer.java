package com.esc.bluespring.common.utils.querydsl;

import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

public class RepositorySlicer {

  public static <T> Slice<T> toSlice(List<T> contents, Pageable pageable) {
    boolean hasNext = checkHasNext(contents, pageable);
    return new SliceImpl<>(hasNext ? removeLast(contents, pageable) : contents, pageable, hasNext);
  }

  private static <T> boolean checkHasNext(List<T> content, Pageable pageable) {
    return pageable.isPaged() && content.size() > pageable.getPageSize();
  }

  private static <T> List<T> removeLast(List<T> content, Pageable pageable) {
    return content.subList(0, pageable.getPageSize());
  }

}

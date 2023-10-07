package com.esc.bluespring.common;

import com.esc.bluespring.common.entity.BaseEntity;
import com.esc.bluespring.common.entity.InformationEntity;
import java.util.Collection;
import org.hibernate.Hibernate;

public interface LazyLoadingAwareMapper {
  default boolean isNotLazyLoaded(Collection<?> sourceCollection){
    return Hibernate.isInitialized(sourceCollection);
  }

  default boolean isNotLazyLoaded(BaseEntity entity) {
    return Hibernate.isInitialized(entity);
  }

  default boolean isNotLazyLoaded(InformationEntity entity) {
    return Hibernate.isInitialized(entity);
  }
}

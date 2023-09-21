package com.esc.bluespring.common.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class InformationEntity {
  @Id
  private Long id;
  @Column(updatable = false)
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;

  @PreUpdate
  public void preUpdate() {
    updatedAt = LocalDateTime.now(ZoneOffset.UTC);
  }

  @PrePersist
  public void prePersist() {
    updatedAt = LocalDateTime.now(ZoneOffset.UTC);
    createdAt = LocalDateTime.now(ZoneOffset.UTC);
  }

  protected InformationEntity(Long id) {
    this.id = id;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    InformationEntity that = (InformationEntity) o;
    return Objects.equals(id, that.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }
}

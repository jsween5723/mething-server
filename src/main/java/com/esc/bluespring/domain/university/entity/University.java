package com.esc.bluespring.domain.university.entity;

import com.esc.bluespring.common.entity.BaseEntity;
import com.esc.bluespring.domain.locationDistrict.entity.LocationDistrict;
import com.esc.bluespring.domain.university.major.entity.Major;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "universities")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class University extends BaseEntity implements Serializable {
  private String name;
  private String campus;
  private String type;
  @JoinColumn(name = "location_district_id")
  @ManyToOne(fetch = FetchType.LAZY)
  private LocationDistrict locationDistrict;

  @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Major> majors = new ArrayList<>();

  @Builder
  public University(Long id, String name, String campus, String type,
      LocationDistrict locationDistrict) {
    super(id);
    this.name = name;
    this.campus = campus;
    this.type = type;
    this.locationDistrict = locationDistrict;
  }
  public University(Long id) {
    super(id);
  }
  public void changeLocation(LocationDistrict locationDistrict) {
    this.locationDistrict = locationDistrict;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
//    if (!super.equals(o)) {
//      return false;
//    }
    University that = (University) o;
    return Objects.equals(name, that.name) && Objects.equals(campus,
        that.campus) && Objects.equals(type, that.type) && Objects.equals(
        locationDistrict.getId(), that.locationDistrict.getId());
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), name, campus, type, locationDistrict.getId());
  }
}

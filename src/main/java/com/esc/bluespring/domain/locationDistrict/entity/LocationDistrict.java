package com.esc.bluespring.domain.locationDistrict.entity;

import com.esc.bluespring.common.entity.InformationEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.HashSet;
import java.util.Set;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "location_districts")
@NoArgsConstructor
public class LocationDistrict extends InformationEntity {
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "parent")
    private final Set<LocationDistrict> children = new HashSet<>();
    private String name;
    @JoinColumn(name = "parent_id")
    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
    private LocationDistrict parent;

    @Builder
    public LocationDistrict(Long id, String name, LocationDistrict parent) {
        super(id);
        this.name = name;
        this.parent = parent;
    }
    public void addChildren(LocationDistrict child) {
        children.add(child);
        child.changeParent(this);
    }

    private void changeParent(LocationDistrict parent) {
        this.parent = parent;
    }

    public boolean isParentOf(LocationDistrict child) {
        return children.contains(child) && child.getParent() != null && child.getParent()
            .equals(this);
    }
}

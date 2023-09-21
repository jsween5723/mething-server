package com.esc.bluespring.domain.locationDistrict.repository;

import static com.esc.bluespring.domain.locationDistrict.entity.QLocationDistrict.locationDistrict;

import com.esc.bluespring.common.utils.querydsl.RepositorySlicer;
import com.esc.bluespring.domain.locationDistrict.classes.LocationDistrictDto.MainPageListElement;
import com.esc.bluespring.domain.locationDistrict.classes.LocationDistrictDto.SearchCondition;
import com.esc.bluespring.domain.locationDistrict.entity.LocationDistrict;
import com.esc.bluespring.domain.locationDistrict.entity.QLocationDistrict;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.ConstructorExpression;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
public class LocationDistrictQDRImpl implements LocationDistrictQDR {

    private final JPAQueryFactory query;

    @Transactional(readOnly = true)
    public Slice<LocationDistrict> search(SearchCondition condition, Pageable pageable) {
        List<LocationDistrict> result = query.selectFrom(locationDistrict)
            .leftJoin(locationDistrict.parent).fetchJoin()
            .where(toWhereCondition(condition))
            .orderBy(locationDistrict.parent.name.asc(),
                locationDistrict.name.asc())
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize()+1)
            .fetch();
        return RepositorySlicer.toSlice(result, pageable);
    }

    public BooleanBuilder toWhereCondition(SearchCondition condition) {
        BooleanBuilder builder = new BooleanBuilder();
        if (condition.name() != null) {
            builder.and(locationDistrict.name.contains(condition.name())
                .or(locationDistrict.parent.name.contains(condition.name())));
        }
        if (condition.parentId() != null) {
            builder.and(locationDistrict.parent.id.eq(condition.parentId()));
        }
        return builder;
    }

    public ConstructorExpression<MainPageListElement> toMainPageListElement(QLocationDistrict locationDistrict) {
        return Projections.constructor(MainPageListElement.class, locationDistrict.id, locationDistrict.name);
    }
}

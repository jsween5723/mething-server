package com.esc.bluespring.domain.university.repository;

import static com.esc.bluespring.domain.locationDistrict.entity.QLocationDistrict.locationDistrict;
import static com.esc.bluespring.domain.university.entity.QUniversity.university;

import com.esc.bluespring.common.utils.querydsl.RepositorySlicer;
import com.esc.bluespring.domain.locationDistrict.repository.LocationDistrictQDRImpl;
import com.esc.bluespring.domain.university.classes.UniversityDto.MainPageListElement;
import com.esc.bluespring.domain.university.classes.UniversityDto.SearchCondition;
import com.esc.bluespring.domain.university.entity.QUniversity;
import com.esc.bluespring.domain.university.entity.University;
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
public class UniversityQDRImpl implements UniversityQDR {

    private final JPAQueryFactory query;
    private final LocationDistrictQDRImpl locationDistrictQDR;

    @Transactional(readOnly = true)
    public Slice<University> search(SearchCondition condition, Pageable pageable) {
        List<University> content = query.selectFrom(university)
            .innerJoin(university.locationDistrict, locationDistrict).fetchJoin()
            .leftJoin(locationDistrict.parent).fetchJoin().where(toWhereCondition(condition))
            .offset(pageable.getOffset()).limit(pageable.getPageSize() + 1)
            .orderBy(university.name.asc(), university.campus.asc()).fetch();
        return RepositorySlicer.toSlice(content, pageable);
    }

    private BooleanBuilder toWhereCondition(SearchCondition condition) {
        BooleanBuilder builder = new BooleanBuilder();
        if (condition.name() != null) {
            String trimed = condition.name().trim();
            if (!trimed.isBlank()) {
                builder.and(
                    university.name.contains(trimed).or(university.campus.contains(trimed)));
            }
        }
        return builder;
    }

    public ConstructorExpression<MainPageListElement> toMainPageListElement(
        QUniversity university) {
        return Projections.constructor(MainPageListElement.class, university.id, university.name, university.campus,
            university.type, locationDistrictQDR.toMainPageListElement(university.locationDistrict));
    }
}

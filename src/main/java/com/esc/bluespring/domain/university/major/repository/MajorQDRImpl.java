package com.esc.bluespring.domain.university.major.repository;

import static com.esc.bluespring.domain.locationDistrict.entity.QLocationDistrict.locationDistrict;
import static com.esc.bluespring.domain.university.entity.QUniversity.university;
import static com.esc.bluespring.domain.university.major.entity.QMajor.major;

import com.esc.bluespring.common.utils.querydsl.RepositorySlicer;
import com.esc.bluespring.domain.university.major.classes.MajorDto.SearchCondition;
import com.esc.bluespring.domain.university.major.entity.Major;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
public class MajorQDRImpl implements MajorQDR{
    private final JPAQueryFactory query;

    @Transactional(readOnly = true)
    public Slice<Major> search(SearchCondition condition, Pageable pageable) {
        List<Major> content = query.selectFrom(major)
            .leftJoin(major.university, university).fetchJoin()
            .leftJoin(university.locationDistrict, locationDistrict).fetchJoin()
            .leftJoin(locationDistrict.parent).fetchJoin()
            .where(toWhereCondition(condition))
            .orderBy(major.university.name.asc(), major.name.asc())
            .limit(pageable.getPageSize())
            .offset(pageable.getOffset())
            .fetch();
        return RepositorySlicer.toSlice(content, pageable);
    }
    private BooleanBuilder toWhereCondition(SearchCondition condition) {
        BooleanBuilder builder = new BooleanBuilder();
        if(condition.universityId() != null) {
            builder.and(major.university.id.eq(condition.universityId()));
        }
        if(condition.name() != null && !condition.name().isBlank()) {
            builder.and(major.name.contains(condition.name().trim()));
        }
        return builder;
    }
}

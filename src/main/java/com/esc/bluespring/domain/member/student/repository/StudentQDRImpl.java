package com.esc.bluespring.domain.member.student.repository;

import static com.esc.bluespring.domain.member.entity.QStudent.student;

import com.esc.bluespring.common.utils.querydsl.RepositorySlicer;
import com.esc.bluespring.domain.member.entity.Student;
import com.esc.bluespring.domain.member.student.classes.StudentDto.SearchCondition;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

@RequiredArgsConstructor
public class StudentQDRImpl implements StudentQDR{
    private final JPAQueryFactory query;
    @Override
    public Slice<Student> searchForAdmin(SearchCondition condition, Pageable pageable) {
        List<Student> result = query.selectFrom(student)
            .leftJoin(student.schoolInformation.major).fetchJoin()
            .leftJoin(student.schoolInformation.major.university).fetchJoin()
            .leftJoin(student.schoolInformation.major.university.locationDistrict).fetchJoin()
            .where(toWhereCondition(condition))
            .limit(pageable.getPageSize() + 1)
            .offset(pageable.getOffset())
            .orderBy(student.schoolInformation.isCertificated.desc())
            .fetch();
        return RepositorySlicer.toSlice(result, pageable);
    }

    private BooleanBuilder toWhereCondition(SearchCondition condition) {
        BooleanBuilder builder = new BooleanBuilder();
        if (condition.isCertificated() != null) {
            builder.and(student.schoolInformation.isCertificated.eq(condition.isCertificated()));
        }
        if (condition.email() != null) {
            builder.and(student.email.contains(condition.email().trim()));
        }
        if (condition.name() != null) {
            builder.and(student.schoolInformation.name.contains(condition.name().trim()));
        }
        if (condition.universityId() != null) {
            builder.and(student.schoolInformation.major.university.id.eq(condition.universityId()));
        }
        if (condition.majorId() != null) {
            builder.and(student.schoolInformation.major.id.eq(condition.majorId()));
        }
        if (condition.nickname() != null) {
            builder.and(student.nickname.contains(condition.nickname().trim()));
        }
        return builder;
    }
}

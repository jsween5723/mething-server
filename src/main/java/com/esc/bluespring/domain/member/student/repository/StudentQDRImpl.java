package com.esc.bluespring.domain.member.student.repository;

import static com.esc.bluespring.domain.member.entity.QStudent.student;

import com.esc.bluespring.common.utils.querydsl.RepositorySlicer;
import com.esc.bluespring.domain.locationDistrict.entity.QLocationDistrict;
import com.esc.bluespring.domain.member.entity.QStudent;
import com.esc.bluespring.domain.member.entity.Student;
import com.esc.bluespring.domain.member.student.classes.StudentDto.AdminStudentSearchCondition;
import com.esc.bluespring.domain.member.student.classes.StudentDto.StudentSearchCondition;
import com.esc.bluespring.domain.university.entity.QUniversity;
import com.esc.bluespring.domain.university.major.entity.QMajor;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

@RequiredArgsConstructor
public class StudentQDRImpl implements StudentQDR {

    private final JPAQueryFactory query;

    @Override
    public Slice<Student> searchForAdmin(AdminStudentSearchCondition condition, Pageable pageable) {
        JPAQuery<Student> dsl = query.selectFrom(student);
        fetchJoinStudent(dsl, student, "student");
        List<Student> result = dsl.where(toAdminWhereCondition(condition))
            .limit(pageable.getPageSize() + 1).offset(pageable.getOffset())
            .orderBy(student.schoolInformation.isCertificated.desc()).fetch();
        return RepositorySlicer.toSlice(result, pageable);
    }

    public Slice<Student> searchByNickname(StudentSearchCondition condition, Pageable pageable) {
        JPAQuery<Student> dsl = query.selectFrom(student);
        fetchJoinStudent(dsl, student, "student");
        List<Student> result = dsl.where(toWhereCondition(condition))
            .limit(pageable.getPageSize() + 1).offset(pageable.getOffset())
            .orderBy(student.schoolInformation.isCertificated.desc()).fetch();
        return RepositorySlicer.toSlice(result, pageable);
    }

    private BooleanBuilder toWhereCondition(StudentSearchCondition condition) {
        BooleanBuilder builder = new BooleanBuilder();
        if (condition.nickname() != null) {
            builder.and(student.nickname.contains(condition.nickname().trim()));
        }
        return builder;
    }

    private BooleanBuilder toAdminWhereCondition(AdminStudentSearchCondition condition) {
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

    public void fetchJoinStudent(JPAQuery<?> query, QStudent student, String key) {
        QMajor qMajor = new QMajor(key + "major");
        QUniversity qUniversity = new QUniversity(key + "university");
        QLocationDistrict qLocationDistrict = new QLocationDistrict(key + "locationDistrict");
        query.leftJoin(student.schoolInformation.major, qMajor).fetchJoin()
            .leftJoin(qMajor.university, qUniversity).fetchJoin()
            .leftJoin(qUniversity.locationDistrict, qLocationDistrict).fetchJoin();
    }
}

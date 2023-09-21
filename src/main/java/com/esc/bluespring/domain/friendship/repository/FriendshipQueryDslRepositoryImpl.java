package com.esc.bluespring.domain.friendship.repository;

import static com.esc.bluespring.domain.friendship.entity.QFriendship.friendship;
import static com.esc.bluespring.domain.locationDistrict.entity.QLocationDistrict.locationDistrict;
import static com.esc.bluespring.domain.member.entity.QStudent.student;
import static com.esc.bluespring.domain.university.entity.QUniversity.university;
import static com.esc.bluespring.domain.university.major.entity.QMajor.major;

import com.esc.bluespring.common.utils.querydsl.RepositorySlicer;
import com.esc.bluespring.domain.friendship.classes.FriendshipDto.SearchCondition;
import com.esc.bluespring.domain.friendship.entity.Friendship;
import com.esc.bluespring.domain.member.entity.Member;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class FriendshipQueryDslRepositoryImpl implements FriendshipQueryDslRepository {
    private final JPAQueryFactory query;
    @Override
    public Slice<Friendship> search(SearchCondition condition, Member user, Pageable pageable) {
        List<Friendship> fetched = query.selectFrom(friendship)
            .join(friendship.friend, student).fetchJoin()
            .join(student.schoolInformation.major, major).fetchJoin()
            .join(major.university, university).fetchJoin()
            .join(university.locationDistrict, locationDistrict).fetchJoin()
            .where(friendship.member.id.eq(user.getId()), toCondition(condition))
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize() + 1L)
            .orderBy(friendship.friend.nickname.asc())
            .fetch();

        return RepositorySlicer.toSlice(fetched, pageable);
    }

    private BooleanBuilder toCondition(SearchCondition condition) {
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        if(condition.universityName() != null) {
            booleanBuilder.and(university.name.contains(condition.universityName()));
        }
        if(condition.nickname() != null) {
            booleanBuilder.and(student.nickname.contains(condition.nickname()));
        }
        if(condition.locationDistrictName() != null) {
            booleanBuilder.and(university.locationDistrict.name.contains(condition.locationDistrictName()));
        }
        if(condition.universityId() != null) {
            booleanBuilder.and(university.id.eq(condition.universityId()));
        }
        return booleanBuilder;
    }
}

package com.esc.bluespring.domain.friendship.request.repository;

import static com.esc.bluespring.domain.friendship.request.entity.QFriendshipRequest.friendshipRequest;

import com.esc.bluespring.domain.friendship.request.classes.FriendshipRequestDto.SearchCondition;
import com.esc.bluespring.domain.friendship.request.entity.FriendshipRequest;
import com.esc.bluespring.domain.locationDistrict.entity.QLocationDistrict;
import com.esc.bluespring.domain.member.entity.QStudent;
import com.esc.bluespring.domain.university.entity.QUniversity;
import com.esc.bluespring.domain.university.major.entity.QMajor;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
class FriendshipRequestQueryDslRepositoryImpl implements FriendshipRequestQueryDslRepository {

    private final JPAQueryFactory query;

    @Override
    public Slice<FriendshipRequest> search(SearchCondition condition, Pageable pageable) {
        QStudent requester = new QStudent("requester");
        QStudent target = new QStudent("target");
        QMajor requesterMajor = new QMajor("requester");
        QMajor targetMajor = new QMajor("target");
        QUniversity requesterUniversity = new QUniversity("requester");
        QUniversity targetUniversity = new QUniversity("target");
        QLocationDistrict targetUniversityLocation = new QLocationDistrict("target");
        QLocationDistrict requesterUniversityLocation = new QLocationDistrict("requester");
        List<FriendshipRequest> result = query.selectFrom(friendshipRequest)
            .join(friendshipRequest.target, target).fetchJoin()
            .join(target.schoolInformation.major, targetMajor).fetchJoin()
            .join(targetMajor.university, targetUniversity).fetchJoin()
            .join(targetUniversity.locationDistrict, targetUniversityLocation).fetchJoin()
            .join(friendshipRequest.requester, requester).fetchJoin()
            .join(requester.schoolInformation.major, requesterMajor).fetchJoin()
            .join(requesterMajor.university, requesterUniversity).fetchJoin()
            .join(requesterUniversity.locationDistrict, requesterUniversityLocation).fetchJoin()
            .where(toWhereCondition(condition))
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize() + 1L)
            .fetch();
        boolean hasNext = false;
        if (result.size() > pageable.getPageSize()) {
            result.remove(pageable.getPageSize());
            hasNext = true;
        }
        return new SliceImpl<>(result, pageable, hasNext);
    }

    private Predicate toWhereCondition(SearchCondition condition) {
        BooleanBuilder booleanBuilder = new BooleanBuilder();
//        if (isNotBlank(condition.universityName())) {
//            booleanBuilder.and(friendshipRequest.requester.university.universityName.name.contains(
//                condition.universityName()));
//        }
//        if (isNotBlank(condition.requesterNickname())) {
//            booleanBuilder.and(
//                friendshipRequest.requester.nickname.contains(condition.requesterNickname()));
//        }
//        if (isNotBlank(condition.targetNickname())) {
//            booleanBuilder.and(
//                friendshipRequest.target.nickname.contains(condition.targetNickname()));
//        }
        if (condition.targetId() != null) {
            booleanBuilder.and(friendshipRequest.target.id.eq(condition.targetId()));
        }
        return booleanBuilder;
    }

//    private boolean isNotBlank(String target) {
//        return target == null || !target.isBlank();
//    }
}

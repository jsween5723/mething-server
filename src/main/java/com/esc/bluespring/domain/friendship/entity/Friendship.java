package com.esc.bluespring.domain.friendship.entity;

import com.esc.bluespring.common.entity.BaseEntity;
import com.esc.bluespring.domain.member.entity.Student;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "friendships", uniqueConstraints = {
    @UniqueConstraint(name = "relation", columnNames = {"member_id", "friend_id"})})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Friendship extends BaseEntity {

  @JoinColumn(name = "member_id")
  @ManyToOne(fetch = FetchType.LAZY)
  private Student member;

  @JoinColumn(name = "friend_id")
  @ManyToOne(fetch = FetchType.LAZY)
  private Student friend;

  @Builder
  Friendship(Student member, Student friend) {
    this.member = member;
    this.friend = friend;
  }
}

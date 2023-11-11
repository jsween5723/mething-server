package com.esc.bluespring.domain.member.entity;

import com.esc.bluespring.common.enums.Gender;
import com.esc.bluespring.domain.auth.exception.AuthException.ForbiddenException;
import com.esc.bluespring.domain.file.entity.Image;
import com.esc.bluespring.domain.friendship.entity.Friendship;
import com.esc.bluespring.domain.friendship.request.entity.FriendshipRequest;
import com.esc.bluespring.domain.meeting.entity.MeetingWatchlistItem;
import com.esc.bluespring.domain.member.exception.MemberException.StudentNotCertificatedException;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.persistence.UniqueConstraint;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.BatchSize;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "students", uniqueConstraints = {
    @UniqueConstraint(name = "nickname_constraint", columnNames = {"nickname"})}, indexes = {
    @Index(name = "nickname_index", columnList = "nickname", unique = true),
    @Index(name = "major_index", columnList = "major_id"),
    @Index(name = "name_index", columnList = "name")})
public class Student extends Member {

    @Column(nullable = false, unique = true)
    private String nickname;
    private String introduce;
    @Column(nullable = false)
    private LocalDate birthday;
    @JoinColumn(name = "profile_image_url", referencedColumnName = "url")
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Image profileImage;
    @Column(name = "profile_image_url", insertable = false, updatable = false)
    private String profileImageUrl;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Gender gender;
    @Embedded
    private SchoolInformation schoolInformation;
    @Embedded
    private StudentProfile profile;
    @OneToMany(mappedBy = "owner", cascade = CascadeType.PERSIST)
    @BatchSize(size = 50)
    private List<MeetingWatchlistItem> watchlist = new ArrayList<>();
    @OneToMany(mappedBy = "requester", cascade = CascadeType.ALL, orphanRemoval = true)
    @BatchSize(size = 50)
    private List<FriendshipRequest> friendshipRequests = new ArrayList<>();
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    @BatchSize(size = 50)
    private List<Friendship> friendships = new ArrayList<>();

    public Student(UUID id, String email, String password, String nickname, String introduce,
                   LocalDate birthday, Image profileImage, Gender gender,
                   SchoolInformation schoolInformation, StudentProfile profile) {
        super(id, email, password);
        this.nickname = nickname;
        this.introduce = introduce;
        this.birthday = birthday;
        this.profileImage = profileImage;
        this.gender = gender;
        this.schoolInformation = schoolInformation;
        this.profile = profile;
        profile.assignStudent(this);
    }

    @Transient
    public Integer getAge() {
        long between = ChronoUnit.YEARS.between(birthday, LocalDate.now(ZoneId.of("Asia/Seoul")));
        return Math.toIntExact(between);
    }

    @Override
    public List<Role> getRole() {
        return List.of(Role.STUDENT,
            isCertificated() ? Role.CERTIFICATED_STUDENT : Role.NOT_CERTIFICATED_STUDENT);
    }

    public void validCertificated() {
        if (!schoolInformation.isCertificated()) {
            throw new ForbiddenException();
        }
    }

    public void changeCertificationState(boolean state) {
        getSchoolInformation().changeCertificationState(state);
    }

    public boolean isCertificated() {
        return getSchoolInformation().isCertificated();
    }

    @Override
    public void valid() {
        if (!schoolInformation.isCertificated()) {
            throw new StudentNotCertificatedException();
        }
    }

    public void friendshipRequestTo(Student target, String message) {
        FriendshipRequest request = FriendshipRequest.builder().requester(this).target(target)
            .message(message).build();
        friendshipRequests.add(request);
    }

    public void addFriendship(Student friend) {
        Friendship friendship = Friendship.builder().member(this).friend(friend).build();
        friendships.add(friendship);
    }

    @Override
    public void patch(Member source) {
        super.patch(source);
        if (source.getEmail() != null) {
            schoolInformation.changeCertificationState(false);
        }
        if (source instanceof Student student) {
            if (student.nickname != null) {
                nickname = student.nickname;
            }
            if (student.birthday != null) {
                birthday = student.birthday;
            }
            if (student.profileImage != null) {
                profileImage = student.profileImage;
            }
            if (student.gender != null) {
                gender = student.gender;
            }
            if (student.schoolInformation != null) {
                schoolInformation.patch(student.schoolInformation);
            }
            if (student.introduce != null) {
                introduce = student.introduce;
            }
        }
    }
}

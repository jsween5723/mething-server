package com.esc.bluespring.domain.member.entity;

import com.esc.bluespring.common.enums.Gender;
import com.esc.bluespring.common.enums.MBTI;
import com.esc.bluespring.domain.file.entity.Image;
import com.esc.bluespring.domain.friendship.entity.Friendship;
import com.esc.bluespring.domain.friendship.request.entity.FriendshipRequest;
import com.esc.bluespring.domain.meeting.entity.Meeting;
import com.esc.bluespring.domain.meeting.watchlist.entity.MeetingWatchlistItem;
import com.esc.bluespring.domain.member.exception.MemberException.StudentNotCertificatedException;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Student extends Member{
    @Column(nullable = false)
    private String nickname;
    @Column(nullable = false)
    private LocalDateTime birthday;
    @JoinColumn(name = "profile_image_url", referencedColumnName = "url")
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Image profileImage;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Gender gender;
    @Enumerated(EnumType.STRING)
    private MBTI mbti;
    @Embedded
    private SchoolInformation schoolInformation;
    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MeetingWatchlistItem> watchlist = new ArrayList<>();
    @OneToMany(mappedBy = "requester", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FriendshipRequest> friendshipRequests = new ArrayList<>();
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Friendship> friendships = new ArrayList<>();

    public Student(Long id, String email, String password, String nickname, LocalDateTime birthday,
        Image profileImage, Gender gender, MBTI mbti, SchoolInformation schoolInformation) {
        super(id, email, password);
        this.nickname = nickname;
        this.birthday = birthday;
        this.profileImage = profileImage;
        this.gender = gender;
        this.mbti = mbti;
        this.schoolInformation = schoolInformation;
    }

    @Override
    public void valid() {
        if (!schoolInformation.isCertificated()) {
            throw new StudentNotCertificatedException();
        }
    }

    public void friendshipRequestTo(Student target, String message) {
        FriendshipRequest request = FriendshipRequest.builder()
            .requester(this)
            .target(target)
            .message(message)
            .build();
        friendshipRequests.add(request);
    }

    public void addFriendship(Student friend) {
        Friendship friendship = Friendship.builder()
            .member(this)
            .friend(friend)
            .build();
        friendships.add(friendship);
    }

    public void addWatchlist(Meeting meetingOwnerTeam) {
        MeetingWatchlistItem item = MeetingWatchlistItem.builder()
            .meeting(meetingOwnerTeam)
            .owner(this)
            .build();
        watchlist.add(item);
    }
}

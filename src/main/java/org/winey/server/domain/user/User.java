package org.winey.server.domain.user;

import static org.winey.server.domain.user.UserLevel.COMMONER;

import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.winey.server.domain.AuditingTimeEntity;
import org.winey.server.domain.comment.Comment;
import org.winey.server.domain.feed.Feed;
import org.winey.server.domain.feed.FeedLike;
import org.winey.server.domain.notification.Notification;
import org.winey.server.domain.recommend.Recommend;

@Entity
@Getter
@DynamicInsert
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends AuditingTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(nullable = false, unique = true)
    private String nickname;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UserLevel userLevel;

    @Column(nullable = false)
    private String socialId;

    @Column(nullable = true)
    private String refreshToken;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private SocialType socialType;

    @Column(nullable = true)
    private String fcmToken;

    @Column(nullable = true)
    private Boolean fcmIsAllowed = true;

    @Column
    private Long savedAmount;

    @Column
    private Long savedCount;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, mappedBy = "user", orphanRemoval = true)
    private List<Recommend> recommends;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, mappedBy = "user", orphanRemoval = true)
    private List<Feed> feeds;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, mappedBy = "user", orphanRemoval = true)
    private List<Comment> comments;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, mappedBy = "user", orphanRemoval = true)
    private List<FeedLike> feedLikes;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, mappedBy = "notiReceiver", orphanRemoval = true)
    private List<Notification> notifications;

    @Builder
    public User(String nickname, String socialId, SocialType socialType) {
        this.nickname = nickname;
        this.userLevel = COMMONER;
        this.socialId = socialId;
        this.socialType = socialType;
        this.savedCount = 0L;
        this.savedAmount = 0L;
    }

    public void updateUserLevel(UserLevel userLevel){
        this.userLevel = userLevel;
    }

    public void upgradeUserLevel() {
        switch (this.userLevel) {
            case COMMONER:
                this.userLevel = UserLevel.KNIGHT;
                break;
            case KNIGHT:
                this.userLevel = UserLevel.ARISTOCRAT;
                break;
            case ARISTOCRAT:
                this.userLevel = UserLevel.EMPEROR;
                break;
            case EMPEROR:
                break;
        }
    }

    public void updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public void updateNickname(String nickname) {
        this.nickname = nickname;
    }

    public void updateFcmToken(String fcmToken) { this.fcmToken = fcmToken; }

    public void updateFcmIsAllowed(Boolean isAllowed){this.fcmIsAllowed = isAllowed;}

    public void increaseSavedAmountAndCount(Long money) {
        this.savedAmount += money;
        this.savedCount += 1;
    }

    public void decreaseSavedAmountAndCount(Long money) {
        this.savedCount -= 1;
        this.savedAmount -= money;
    }

    public String getFcmToken() {
        if (Objects.nonNull(this.fcmToken)) {
            return this.fcmToken;
        }
        return null;
    }
}

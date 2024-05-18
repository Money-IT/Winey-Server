package org.winey.server.infrastructure;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.lang.Nullable;
import org.winey.server.domain.feed.Feed;
import org.winey.server.domain.recommend.Recommend;
import org.winey.server.domain.user.SocialType;
import org.winey.server.domain.user.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends Repository<User, Long> {
    // CREATE
    void save(User user);

    // READ
    Optional<User> findByUserId(Long userId);

    List<User> findByFcmTokenNotNull();


    Boolean existsBySocialIdAndSocialType(String socialId, SocialType socialType);

    Optional<User> findBySocialIdAndSocialType(String socialId, SocialType socialType);

    Optional<User> findByRefreshToken(String refreshToken);

    Boolean existsByNickname(String nickname);


    // UPDATE

    // DELETE
    long deleteByUserId(Long userId);

}

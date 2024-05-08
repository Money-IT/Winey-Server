package org.winey.server.service.auth;

import java.util.Random;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.winey.server.config.jwt.JwtService;
import org.winey.server.controller.request.auth.SignInRequestDto;
import org.winey.server.controller.response.auth.SignInResponseDto;
import org.winey.server.controller.response.auth.TokenResponseDto;
import org.winey.server.domain.goal.Goal;
import org.winey.server.domain.goal.GoalType;
import org.winey.server.domain.notification.NotiType;
import org.winey.server.domain.notification.Notification;
import org.winey.server.domain.user.SocialType;
import org.winey.server.domain.user.User;
import org.winey.server.exception.Error;
import org.winey.server.exception.model.NotFoundException;
import org.winey.server.exception.model.UnprocessableEntityException;
import org.winey.server.infrastructure.BlockUserRepository;
import org.winey.server.infrastructure.GoalRepository;
import org.winey.server.infrastructure.NotiRepository;
import org.winey.server.infrastructure.UserRepository;
import org.winey.server.service.auth.apple.AppleSignInService;
import org.winey.server.service.auth.kakao.KakaoSignInService;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AppleSignInService appleSignInService;
    private final KakaoSignInService kakaoSignInService;
    private final JwtService jwtService;

    private final UserRepository userRepository;
    private final BlockUserRepository blockUserRepository;
    private final GoalRepository goalRepository;


    private final Long TOKEN_EXPIRATION_TIME_ACCESS = 100 * 24 * 60 * 60 * 1000L;
    private final Long TOKEN_EXPIRATION_TIME_REFRESH = 200 * 24 * 60 * 60 * 1000L;

    private final NotiRepository notiRepository;

    @Transactional
    public SignInResponseDto signIn(String socialAccessToken, SignInRequestDto requestDto) {
        SocialType socialType = SocialType.valueOf(requestDto.getSocialType());
        String socialId = login(socialType, socialAccessToken);

        Boolean isRegistered = userRepository.existsBySocialIdAndSocialType(socialId, socialType);

        if (!isRegistered) {
            String randomString= new Random().ints(6, 0, 36).mapToObj(i -> Character.toString("abcdefghijklmnopqrstuvwxyz0123456789".charAt(i))).collect(Collectors.joining());
            while (userRepository.existsByNickname("위니"+randomString)) {
                randomString = new Random().ints(6, 0, 36).mapToObj(i -> Character.toString("abcdefghijklmnopqrstuvwxyz0123456789".charAt(i))).collect(Collectors.joining());
            }

            User newUser = User.builder()
                    .nickname("위니"+randomString)
                    .socialId(socialId)
                    .socialType(socialType).build();
            newUser.updateFcmIsAllowed(true); //신규 유저면 true박고
            userRepository.save(newUser);


            Notification newNoti = Notification.builder()
                    .notiReciver(newUser)
                    .notiMessage(NotiType.HOWTOLEVELUP.getType())
                    .isChecked(false)
                    .notiType(NotiType.HOWTOLEVELUP)
                    .build();
            newNoti.updateLinkId(null);
            notiRepository.save(newNoti);

            Goal newGoal = Goal.builder()
                .goalType(GoalType.COMMONER_GOAL)
                .user(newUser)
                .build();
            goalRepository.save(newGoal);
        }

        User user = userRepository.findBySocialIdAndSocialType(socialId, socialType)
                .orElseThrow(() -> new NotFoundException(Error.NOT_FOUND_USER_EXCEPTION, Error.NOT_FOUND_USER_EXCEPTION.getMessage()));

        // jwt 발급 (액세스 토큰, 리프레쉬 토큰)
        String accessToken = jwtService.issuedToken(String.valueOf(user.getUserId()), TOKEN_EXPIRATION_TIME_ACCESS);
        String refreshToken = jwtService.issuedToken(String.valueOf(user.getUserId()), TOKEN_EXPIRATION_TIME_REFRESH);
        String fcmToken = requestDto.getFcmToken();

        user.updateRefreshToken(refreshToken);
        user.updateFcmToken(fcmToken);

        return SignInResponseDto.of(user.getUserId(), accessToken, refreshToken, fcmToken, isRegistered,user.getFcmIsAllowed());
    }

    @Transactional
    public TokenResponseDto issueToken(String refreshToken) {
        jwtService.verifyToken(refreshToken);

        User user = userRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new NotFoundException(Error.NOT_FOUND_USER_EXCEPTION, Error.NOT_FOUND_USER_EXCEPTION.getMessage()));

        // jwt 발급 (액세스 토큰, 리프레쉬 토큰)
        String newAccessToken = jwtService.issuedToken(String.valueOf(user.getUserId()), TOKEN_EXPIRATION_TIME_ACCESS);
        String newRefreshToken = jwtService.issuedToken(String.valueOf(user.getUserId()), TOKEN_EXPIRATION_TIME_REFRESH);

        user.updateRefreshToken(newRefreshToken);

        return TokenResponseDto.of(newAccessToken, newRefreshToken);
    }

    @Transactional
    public void signOut(Long userId) {
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new NotFoundException(Error.NOT_FOUND_USER_EXCEPTION, Error.NOT_FOUND_USER_EXCEPTION.getMessage()));
        user.updateRefreshToken(null);
        user.updateFcmToken(null);
    }

    private String login(SocialType socialType, String socialAccessToken) {
        if (socialType.toString() == "APPLE") {
            return appleSignInService.getAppleId(socialAccessToken);
        }
        else if (socialType.toString() == "KAKAO") {
            return kakaoSignInService.getKaKaoId(socialAccessToken);
        }
        else{
            return "ads";
        }
    }

    @Transactional
    public void withdraw(Long userId){
        User user = userRepository.findByUserId(userId).orElse(null);
        System.out.println(userId);
        if (user == null) {
            throw new NotFoundException(Error.NOT_FOUND_USER_EXCEPTION, Error.NOT_FOUND_USER_EXCEPTION.getMessage());
        }
        System.out.println("User: " + user);
        System.out.println("Goals: " + user.getGoals());
        System.out.println("Recommends: " + user.getRecommends());
        System.out.println("Feeds: " + user.getFeeds());
        System.out.println("FeedLikes: " + user.getFeedLikes());
        System.out.println("Comments: "+ user.getComments());

        // 유저가 생성한 반응과 관련된 알림 삭제
        notiRepository.deleteByRequestUserId(userId);
        
        blockUserRepository.deleteByRequestUser(user);
        blockUserRepository.deleteByResponseUser(user);

        Long res = userRepository.deleteByUserId(userId); //res가 삭제된 컬럼의 개수 즉, 1이 아니면 뭔가 알 수 없는 에러.
        System.out.println(res + "개의 컬럼이 삭제되었습니다.");
        if (res!=1){
            throw new UnprocessableEntityException(Error.UNPROCESSABLE_ENTITY_DELETE_EXCEPTION, Error.UNPROCESSABLE_ENTITY_DELETE_EXCEPTION.getMessage());
        }
    }
}

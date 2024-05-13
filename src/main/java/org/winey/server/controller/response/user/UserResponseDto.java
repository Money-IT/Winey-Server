package org.winey.server.controller.response.user;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class UserResponseDto {

    private UserData userData;

    @Getter
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class UserData {
        private Long userId;
        private Long createdDday;
        private String nickname;
        private String userLevel;
        private Boolean fcmIsAllowed;
        private Long accumulatedAmount;
        private Long accumulatedCount;
        private Long amountSavedTwoWeeks;
        private Long amountSpentTwoWeeks;
        private Long remainingAmount;
        private Long remainingCount;
    }

    public static UserResponseDto of(Long userId, Long createdDday, String nickname, String userLevel,
        Boolean fcmIsAllowed, Long accumulatedAmount,Long accumulatedCount, Long amountSavedTwoWeeks,
        Long amountSpentTwoWeeks, Long remainingAmount, Long remainingCount) {
        UserData userData = new UserData(userId, createdDday, nickname, userLevel, fcmIsAllowed, accumulatedAmount, accumulatedCount,
            amountSavedTwoWeeks, amountSpentTwoWeeks,remainingAmount,remainingCount);
        return new UserResponseDto(userData);
    }
}

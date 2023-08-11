package org.winey.server.controller.response.notification;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.winey.server.controller.response.feed.GetFeedResponseDto;
import org.winey.server.domain.notification.NotiType;
import org.winey.server.domain.notification.Notification;
import org.winey.server.domain.user.User;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class GetNotiResponseDto {
    private Long notiId;
    // user부분
    private String notiUserName; //알림을 일으킨 유저. 등급 상승등은 본인 닉네임.
    private String notiMessage;
    private NotiType notiType;
    private boolean isChecked; //유저가 이 알림을 체크했는지
    private Integer LinkId; //좋아요, 댓글일 경우에는 feedid를 넘기고 아니면 null이라서 안넘어감.

    public static GetNotiResponseDto of(Long notiId, String notiUserName, String notiMessage, NotiType notiType, boolean isChecked, Integer linkId){
        return new GetNotiResponseDto(notiId,notiUserName,notiMessage,notiType,isChecked,linkId);
    }
}
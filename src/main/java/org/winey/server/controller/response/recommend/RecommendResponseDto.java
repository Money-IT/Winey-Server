package org.winey.server.controller.response.recommend;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RecommendResponseDto {
    private Long recommendId;
    private String recommendLink;
    private String recommendTitle;
    private String recommendSubTitle;
    private Long recommendWon;
    private Long recommendPercent;
    private String recommendImage;
    private LocalDateTime createdAt;

    public static RecommendResponseDto of(Long recommendId, String recommendLink, String recommendTitle, String recommendSubTitle, Long recommendWon, Long recommendPercent, String recommendImage, LocalDateTime createdAt) {
        return new RecommendResponseDto(recommendId, recommendLink, recommendTitle, recommendSubTitle, recommendWon, recommendPercent, recommendImage, createdAt);
    }
}

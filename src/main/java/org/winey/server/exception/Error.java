package org.winey.server.exception;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum Error {

    /**
     * 400 BAD REQUEST
     */
    REQUEST_VALIDATION_EXCEPTION(HttpStatus.BAD_REQUEST, "잘못된 요청입니다"),
    INVALID_FCMTOKEN_EXCEPTION(HttpStatus.BAD_REQUEST, "푸시알림 동의를 했는데, fcmtoken이 null이거나 문제가 있습니다."),
    LOGIN_SUCCESS(HttpStatus.OK, "로그인에 성공했습니다."),
    INVALID_PASSWORD_EXCEPTION(HttpStatus.BAD_REQUEST, "잘못된 비밀번호가 입력됐습니다."),
    NOT_FOUND_IMAGE_EXCEPTION(HttpStatus.BAD_REQUEST, "잘못된 이미지 파일입니다"),
    INVALID_FEEDTYPE(HttpStatus.BAD_REQUEST, "잘못된 피드유형 입니다"),
    VALIDATION_REQUEST_MISSING_EXCEPTION(HttpStatus.BAD_REQUEST, "요청값이 입력되지 않았습니다."),
    VALIDATION_REQUEST_HEADER_MISSING_EXCEPTION(HttpStatus.BAD_REQUEST, "요청 헤더값이 입력되지 않았습니다."),
    VALIDATION_REQUEST_PARAMETER_MISSING_EXCEPTION(HttpStatus.BAD_REQUEST, "요청 파라미터값이 입력되지 않았습니다."),
    NOT_FOUND_CREATED_AT_EXCEPTION(HttpStatus.BAD_REQUEST, "요청한 피드의 생성일이 존재하지 않습니다."),
    PAGE_REQUEST_VALIDATION_EXCEPTION(HttpStatus.BAD_REQUEST, "페이지 넘버가 유효하지 않습니다."),
    REQUEST_METHOD_VALIDATION_EXCEPTION(HttpStatus.BAD_REQUEST, "요청 메소드가 잘못됐습니다."),
    MAX_UPLOAD_SIZE_EXCEED_EXCEPTION(HttpStatus.PAYLOAD_TOO_LARGE, "파일 용량 초과"),
    MAX_AMOUNT_VALIDATION_EXCEPTION(HttpStatus.BAD_REQUEST, "금액 상한선 초과"),

    INVALID_APPLE_PUBLIC_KEY(HttpStatus.BAD_REQUEST, "Apple JWT 값의 alg, kid 정보가 올바르지 않습니다."),
    INVALID_APPLE_IDENTITY_TOKEN(HttpStatus.BAD_REQUEST, "Apple OAuth Identity Token 형식이 올바르지 않습니다."),
    EXPIRED_APPLE_IDENTITY_TOKEN(HttpStatus.BAD_REQUEST, "Apple OAuth 로그인 중 Identity Token 유효기간이 만료됐습니다."),
    INVALID_APPLE_CLAIMS(HttpStatus.BAD_REQUEST, "Apple OAuth Claims 값이 올바르지 않습니다."),
    INVALID_ENCRYPT_COMMUNICATION(HttpStatus.BAD_REQUEST, "Apple OAuth 통신 암호화 과정 중 문제가 발생했습니다."),
    CREATE_PUBLIC_KEY_EXCEPTION(HttpStatus.BAD_REQUEST, "Apple OAuth 로그인 중 public verify 생성에 문제가 발생했습니다."),
    INVALID_USER_LEVEL_EXCEPTION(HttpStatus.BAD_REQUEST, "존재하지 않는 유저 레벨입니다."),

    /**
     * 401 UNAUTHORIZED
     */
    TOKEN_TIME_EXPIRED_EXCEPTION(HttpStatus.UNAUTHORIZED, "만료된 토큰입니다."),
    DELETE_UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "삭제 요청 권한이 없습니다."),

    /**
     * 403 FORBIDDEN
     */
    FEED_FORBIDDEN_EXCEPTION(HttpStatus.FORBIDDEN,"목표를 설정하지 않아 피드를 생성할 수 없습니다."),

    /**
     * 404 NOT FOUND
     */
    NOT_FOUND_USER_EXCEPTION(HttpStatus.NOT_FOUND, "존재하지 않는 유저입니다."),
    NOT_FOUND_RECOMMEND_PAGE_EXCEPTION(HttpStatus.NOT_FOUND, "존재하지 않는 추천 위니 페이지입니다."),
    NOT_FOUND_GOAL_EXCEPTION(HttpStatus.NOT_FOUND, "존재하지 않는 목표입니다."),
    NOT_FOUND_FEED_EXCEPTION(HttpStatus.NOT_FOUND, "존재하지 않는 피드입니다."),
    NOT_FOUND_COMMENT_EXCEPTION(HttpStatus.NOT_FOUND, "존재하지 않는 댓글입니다."),

    /**
     * 409 CONFLICT
     */
    ALREADY_EXIST_USER_EXCEPTION(HttpStatus.CONFLICT, "이미 존재하는 유저입니다"),
    ALREADY_EXIST_NICKNAME_EXCEPTION(HttpStatus.CONFLICT, "이미 존재하는 닉네임입니다"),


    /**
     * 422 UNPROCESSABLE ENTITY
     */
    UNPROCESSABLE_ENTITY_DELETE_EXCEPTION(HttpStatus.UNPROCESSABLE_ENTITY, "클라의 요청을 이해했지만 삭제하지 못했습니다."),
    UNPROCESSABLE_FIND_USERS(HttpStatus.UNPROCESSABLE_ENTITY, "요청을 이해했지만 유저들을 찾을 수 없었습니다."),
    UNPROCESSABLE_SEND_TO_FIREBASE(HttpStatus.UNPROCESSABLE_ENTITY, "파이어베이스로 전송하는 과정에서 에러가 발생했습니다."),
    UNPROCESSABLE_KAKAO_SERVER_EXCEPTION(HttpStatus.UNPROCESSABLE_ENTITY, "요청을 이해했지만 카카오 서버에러가 발생했습니다."),

    /**
     * 500 INTERNAL SERVER ERROR
     */
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "알 수 없는 서버 에러가 발생했습니다"),
    ;

    private final HttpStatus httpStatus;
    private final String message;

    public int getHttpStatusCode() {
        return httpStatus.value();
    }
}

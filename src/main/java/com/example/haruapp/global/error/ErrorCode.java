package com.example.haruapp.global.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum ErrorCode {


    SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 에러입니다. 서버 팀에 연락주세요!"),
    INVALID_INDEX(HttpStatus.BAD_REQUEST, "잘못된 인덱스입니다."),
    INVALID_INPUT(HttpStatus.BAD_REQUEST, "잘못된 입력입니다."),
    JSON_CONVERT_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "JSON 파싱 실패" ),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "예기치 못한 서버 오류입니다."),
    ACCESS_DENIED(HttpStatus.FORBIDDEN,"권한이 부족합니다." ),

    // 코스
    COURSE_STORE_COUNT_INVALID(HttpStatus.BAD_REQUEST, "코스별 매장 개수는 6개여야 합니다."),
    COURSE_UPDATE_UNAUTHORIZED(HttpStatus.FORBIDDEN, "본인 코스만 수정할 수 있습니다."),
    COURSE_NOT_FOUND(HttpStatus.BAD_REQUEST, "해당 코스가 존재하지 않습니다."),
    COURSE_SCRAP_SELF(HttpStatus.BAD_REQUEST, "본인 코스는 스크랩할 수 없습니다.")
            ;

    // Member
    private final HttpStatus status;
    private final String message;
}

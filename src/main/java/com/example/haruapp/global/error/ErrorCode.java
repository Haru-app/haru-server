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

    SIMILARITY_FETCH_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "파이썬 서버에서 오류가 생겼습니다.");

    // Member
    private final HttpStatus status;
    private final String message;
}

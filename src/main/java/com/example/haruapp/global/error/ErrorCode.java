package com.example.haruapp.global.error;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorCode {

	SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 에러입니다. 서버 팀에 연락주세요!"),
	INVALID_INDEX(HttpStatus.BAD_REQUEST, "잘못된 인덱스입니다."),
	INVALID_INPUT(HttpStatus.BAD_REQUEST, "잘못된 입력입니다."),
	JSON_CONVERT_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "JSON 파싱 실패"),
	INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "예기치 못한 서버 오류입니다."),
	ACCESS_DENIED(HttpStatus.FORBIDDEN, "권한이 부족합니다."),

	// 이미지 카드 생성에 사용
	UNSUPPORTED_IMAGE_FORMAT(HttpStatus.UNSUPPORTED_MEDIA_TYPE, "이미지 파일은 jpg, png 형식만 지원됩니다."),

	;

	// Member
	private final HttpStatus status;

	private final String message;
}

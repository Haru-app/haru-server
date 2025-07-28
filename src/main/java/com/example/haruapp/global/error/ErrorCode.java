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
  	REQUIRE_LOGIN(HttpStatus.UNAUTHORIZED, "해당 기능을 사용하려면 로그인이 필요합니다."),

	// 회원가입에 사용
	DUPLICATE_EMAIL(HttpStatus.BAD_REQUEST, "이미 존재하는 이메일입니다."),
	DUPLICATE_NICKNAME(HttpStatus.BAD_REQUEST, "이미 존재하는 닉네임입니다."),

  ALREADY_SUBSCRIBED(HttpStatus.CONFLICT, "이미 구독된 사용자입니다."),
  USER_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 사용자입니다."),
  TOSS_PAYMENT_FAILED(HttpStatus.BAD_GATEWAY, "토스 결제 URL 생성에 실패했습니다."),

	// 로그인에 사용
	LOGIN_EMAIL_NOT_FOUND(HttpStatus.UNAUTHORIZED, "존재하지 않는 이메일입니다."),
	LOGIN_PASSWORD_MISMATCH(HttpStatus.UNAUTHORIZED, "비밀번호가 일치하지 않습니다."),
	LOGIN_REQUEST_INVALID(HttpStatus.BAD_REQUEST, "이메일 또는 비밀번호가 입력되지 않았습니다."),

  // 코스
  COURSE_STORE_COUNT_INVALID(HttpStatus.BAD_REQUEST, "코스별 매장 개수는 6개여야 합니다."),
  COURSE_UPDATE_UNAUTHORIZED(HttpStatus.FORBIDDEN, "본인 코스만 수정할 수 있습니다."),
  COURSE_NOT_FOUND(HttpStatus.BAD_REQUEST, "해당 코스가 존재하지 않습니다."),
  COURSE_SCRAP_SELF(HttpStatus.BAD_REQUEST, "본인 코스는 스크랩할 수 없습니다."),
  PARSE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "날씨 파싱에러"),
  WEATHERAPI_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR,"날씨 api 서버에러" ),
  SIMILARITY_FETCH_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "파이썬 서버에서 오류가 생겼습니다."),

	// 이미지 카드 생성에 사용
	UNSUPPORTED_IMAGE_FORMAT(HttpStatus.UNSUPPORTED_MEDIA_TYPE, "이미지 파일은 jpg, png 형식만 지원됩니다."),
	IMAGE_PROCESSING_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "이미지 처리 중 오류가 발생했습니다.");

	;


	// Member
	private final HttpStatus status;

	private final String message;
}

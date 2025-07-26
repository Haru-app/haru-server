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

	// 회원가입에 사용
	DUPLICATE_EMAIL(HttpStatus.BAD_REQUEST, "이미 존재하는 이메일입니다."),
	DUPLICATE_NICKNAME(HttpStatus.BAD_REQUEST, "이미 존재하는 닉네임입니다."),

	// 로그인에 사용
	LOGIN_EMAIL_NOT_FOUND(HttpStatus.UNAUTHORIZED, "존재하지 않는 이메일입니다."),
	LOGIN_PASSWORD_MISMATCH(HttpStatus.UNAUTHORIZED, "비밀번호가 일치하지 않습니다."),
	LOGIN_REQUEST_INVALID(HttpStatus.BAD_REQUEST, "이메일 또는 비밀번호가 입력되지 않았습니다.");

	// Member
	private final HttpStatus status;

	private final String message;
}

package com.example.haruapp.auth.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.haruapp.auth.dto.request.LoginRequest;
import com.example.haruapp.auth.dto.request.SignupRequest;
import com.example.haruapp.auth.dto.response.DuplicateCheckResponse;
import com.example.haruapp.auth.dto.response.LoginResponse;
import com.example.haruapp.auth.dto.response.SignupResponse;
import com.example.haruapp.auth.service.MemberService;
import com.example.haruapp.global.error.CustomException;
import com.example.haruapp.global.error.ErrorCode;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class MemberController {

	private final MemberService memberService;

	// 회원가입
	@PostMapping("/join")
	public ResponseEntity<SignupResponse> join(@RequestBody SignupRequest request) {

		SignupResponse response = memberService.register(request);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	// 이메일 중복 검사
	@GetMapping("/check-email")
	public ResponseEntity<DuplicateCheckResponse> checkEmail(@RequestParam(required = false) String email) {

		if (email == null || email.trim().isEmpty()) {
			return ResponseEntity.badRequest()
				.body(new DuplicateCheckResponse(true, "이메일 파라미터가 누락되었습니다."));
		}

		DuplicateCheckResponse response = memberService.checkEmailDuplicate(email);
		return response.isDuplicated()
			? ResponseEntity.status(HttpStatus.CONFLICT).body(response)
			: ResponseEntity.ok(response);
	}

	// 닉네임 중복 검사
	@GetMapping("/check-nickname")
	public ResponseEntity<DuplicateCheckResponse> checkNickname(@RequestParam(required = false) String nickname) {

		if (nickname == null || nickname.trim().isEmpty()) {
			return ResponseEntity.badRequest()
				.body(new DuplicateCheckResponse(true, "닉네임 파라미터가 누락되었습니다."));
		}

		DuplicateCheckResponse response = memberService.checkNicknameDuplicate(nickname);
		return response.isDuplicated()
			? ResponseEntity.status(HttpStatus.CONFLICT).body(response)
			: ResponseEntity.ok(response);
	}

	// 로그인
	@PostMapping("/login")
	public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {

		if (request.getEmail() == null || request.getPassword() == null) {
			throw new CustomException(ErrorCode.LOGIN_REQUEST_INVALID);
		}

		LoginResponse response = memberService.login(request);
		return ResponseEntity.ok(response);
	}

}

package com.example.haruapp.auth.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.haruapp.auth.dto.request.SignupRequest;
import com.example.haruapp.auth.dto.response.SignupResponse;
import com.example.haruapp.auth.service.MemberService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class MemberController {

	private final MemberService memberService;

	@PostMapping("/join")
	public ResponseEntity<SignupResponse> join(@RequestBody SignupRequest request) {
		SignupResponse response = memberService.register(request);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

}

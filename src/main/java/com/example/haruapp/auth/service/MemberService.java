package com.example.haruapp.auth.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.haruapp.auth.domain.Member;
import com.example.haruapp.auth.dto.request.SignupRequest;
import com.example.haruapp.auth.dto.response.SignupResponse;
import com.example.haruapp.auth.mapper.MemberMapper;
import com.example.haruapp.global.error.CustomException;
import com.example.haruapp.global.error.ErrorCode;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberService {
	private final MemberMapper memberMapper;
	private final BCryptPasswordEncoder passwordEncoder;

	public SignupResponse register(SignupRequest request) {
		validateDuplicate(request.getEmail(), request.getNickname());

		Member member = toEntity(request);
		memberMapper.insertMember(member);

		return toResponse(member);
	}

	// 중복 회원 검사
	private void validateDuplicate(String email, String nickname) {
		if (memberMapper.existsByEmail(email)) {
			throw new CustomException(ErrorCode.DUPLICATE_EMAIL);
		}
		if (memberMapper.existsByNickname(nickname)) {
			throw new CustomException(ErrorCode.DUPLICATE_NICKNAME);
		}
	}

	private Member toEntity(SignupRequest request) {
		Member member = new Member();
		member.setEmail(request.getEmail());
		member.setNickname(request.getNickname());
		member.setPassword(passwordEncoder.encode(request.getPassword()));
		return member;
	}

	private SignupResponse toResponse(Member member) {
		return new SignupResponse(
			member.getEmail(),
			member.getNickname(),
			"회원가입이 완료되었습니다."
		);
	}

}

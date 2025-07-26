package com.example.haruapp.auth.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import com.example.haruapp.auth.domain.Member;
import com.example.haruapp.auth.dto.request.SignupRequest;
import com.example.haruapp.auth.dto.response.DuplicateCheckResponse;
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

	public DuplicateCheckResponse checkEmailDuplicate(String email) {
		boolean isDuplicated = memberMapper.existsByEmail(email);
		String message = isDuplicated ? "이미 사용 중인 이메일입니다." : "사용 가능한 이메일입니다.";
		return new DuplicateCheckResponse(isDuplicated, message);
	}

	public DuplicateCheckResponse checkNicknameDuplicate(String nickname) {
		boolean isDuplicated = memberMapper.existsByNickname(nickname);
		String message = isDuplicated ? "이미 사용 중인 닉네임입니다." : "사용 가능한 닉네임입니다.";
		return new DuplicateCheckResponse(isDuplicated, message);
	}
}

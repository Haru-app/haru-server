package com.example.haruapp.auth.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import com.example.haruapp.auth.domain.Member;

@Mapper
public interface MemberMapper {

	// 중복 이메일 존재 여부 확인
	@Select("SELECT COUNT(*) FROM member WHERE email = #{email}")
	boolean existsByEmail(String email);

	// 중복 닉네임 존재 여부 확인
	@Select("SELECT COUNT(*) FROM member WHERE nickname = #{nickname}")
	boolean existsByNickname(String nickname);

	// 멤버 테이블에 회원 삽입
	@Insert("INSERT INTO member (user_id, email, nickname, password) " +
		"VALUES (member_seq.NEXTVAL, #{email}, #{nickname}, #{password})")
	@Options(useGeneratedKeys = true, keyProperty = "userId")
	void insertMember(Member member);
}

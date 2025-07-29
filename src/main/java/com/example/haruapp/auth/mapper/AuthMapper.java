package com.example.haruapp.auth.mapper;

import com.example.haruapp.auth.domain.OauthRequest;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;

import com.example.haruapp.auth.domain.Member;

import java.util.Optional;

@Mapper
public interface AuthMapper {

	// 중복 이메일 존재 여부 확인
	@Select("SELECT CASE WHEN COUNT(*) > 0 THEN 1 ELSE 0 END FROM member WHERE email = #{email}")
	boolean existsByEmail(String email);

	// 중복 닉네임 존재 여부 확인
	@Select("SELECT CASE WHEN COUNT(*) > 0 THEN 1 ELSE 0 END FROM member WHERE nickname = #{nickname}")
	boolean existsByNickname(String nickname);

	// 멤버 테이블에 회원 삽입
	@Insert("INSERT INTO member (user_id, email, nickname, password) " +
		"VALUES (#{userId}, #{email}, #{nickname}, #{password})")
	@SelectKey(statement = "SELECT member_seq.NEXTVAL FROM dual", keyProperty = "userId", before = true, resultType = Long.class)
	void insertMember(Member member);

	// 이메일로 유저 확인
	@Select("SELECT * FROM member WHERE email = #{email}")
	Member findByEmail(String email);

	@Insert("INSERT INTO member (user_id, email, nickname, oauthId, oauth_type) " +
			"VALUES (#{userId}, #{email}, #{nickname}, #{oauthId}, #{oauthType})")
	@SelectKey(statement = "SELECT member_seq.NEXTVAL FROM dual", keyProperty = "userId", before = true, resultType = Long.class)
	void insertKakaoMember(OauthRequest member);

	@Select("SELECT * FROM member WHERE email = #{email} and OAUTHID=#{oauthId}")
	Member findByEmailWithId(String email, String oauthId);
}

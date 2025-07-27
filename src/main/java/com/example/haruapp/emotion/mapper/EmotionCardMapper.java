package com.example.haruapp.emotion.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.example.haruapp.emotion.dto.response.EmotionCardUrlResponse;

@Mapper
public interface EmotionCardMapper {

	@Insert("INSERT INTO emotion_card (emotion_card_id, user_id, course_id, file_path, created_at) " +
		"VALUES (emotion_card_seq.NEXTVAL, #{userId}, #{courseId}, #{filePath}, SYSDATE)")
	void insertEmotionCard(Long userId, Long courseId, String filePath);

	@Select("""
			SELECT file_path as filePath
			FROM emotion_card
			WHERE user_id = #{userId} AND course_id = #{courseId}
			ORDER BY created_at DESC
		""")
	List<EmotionCardUrlResponse> findEmotionCardUrls(@Param("userId") Long userId, @Param("courseId") Long courseId);
}

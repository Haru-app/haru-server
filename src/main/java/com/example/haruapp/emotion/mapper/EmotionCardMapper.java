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
			SELECT file_path AS filePath
			FROM emotion_card
			WHERE user_id = #{userId}
			  AND TO_CHAR(created_at, 'YYYY-MM-DD') = #{date}
			ORDER BY created_at DESC
		""")
	List<EmotionCardUrlResponse> findEmotionCardUrlsByDate(
		@Param("userId") Long userId,
		@Param("date") String date
	);
}

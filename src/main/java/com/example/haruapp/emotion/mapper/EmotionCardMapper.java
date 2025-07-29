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
			SELECT ec.file_path AS filePath, e.EMOTION_NAME as emotion
			FROM emotion_card ec
			join COURSE c on c.COURSE_ID=ec.COURSE_ID
			join EMOTION e on e.EMOTION_ID=c.EMOTION_ID
			WHERE ec.user_id = #{userId}
			  AND TO_CHAR(ec.created_at, 'YYYY-MM-DD') = #{date}
			ORDER BY ec.created_at DESC
		""")
	List<EmotionCardUrlResponse> findEmotionCardUrlsByDate(
		@Param("userId") Long userId,
		@Param("date") String date
	);
}

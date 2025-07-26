package com.example.haruapp.emotion.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface EmotionCardMapper {

	@Insert("INSERT INTO emotion_card (emotion_card_id, user_id, course_id, file_path, created_at) " +
		"VALUES (emotion_card_seq.NEXTVAL, #{userId}, #{courseId}, #{filePath}, SYSDATE)")
	void insertEmotionCard(Long userId, Long courseId, String filePath);
}

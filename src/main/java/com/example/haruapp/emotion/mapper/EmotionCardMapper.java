package com.example.haruapp.emotion.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface EmotionCardMapper {

	@Insert("""
		    INSERT INTO emotion_card (
		        emotion_card_id, user_id, course_id, file_path, is_trial, created_at
		    ) VALUES (
		        emotion_card_seq.NEXTVAL, #{userId}, #{courseId}, #{filePath}, 'N', SYSDATE
		    )
		""")
	void insertEmotionCard(@Param("userId") Long userId, @Param("courseId") Long courseId,
		@Param("filePath") String filePath);
}

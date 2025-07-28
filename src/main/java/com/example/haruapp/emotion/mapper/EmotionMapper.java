package com.example.haruapp.emotion.mapper;

import com.example.haruapp.emotion.dto.EmotionResponse;
import com.example.haruapp.emotion.dto.response.EmotionHistoryResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import java.util.List;

@Mapper
public interface EmotionMapper {

	@Select("""
		    SELECT *
		    FROM (
		        SELECT 
		            e.emotion_id AS emotionId,
		            e.emotion_name AS emotionName,
		            c.created_at AS createdAt,
		            ROW_NUMBER() OVER (
		                PARTITION BY TRUNC(c.created_at) 
		                ORDER BY c.created_at DESC
		            ) AS rn
		        FROM course c
		        JOIN emotion e ON c.emotion_id = e.emotion_id
		        WHERE c.user_id = #{userId}
		          AND EXTRACT(YEAR FROM c.created_at) = #{year}
		          AND EXTRACT(MONTH FROM c.created_at) = #{month}
		    )
		    WHERE rn = 1
		    ORDER BY createdAt DESC
		""")
	List<EmotionHistoryResponse> findEmotionsByUserIdAndMonth(@Param("userId") Long userId,
		@Param("year") int year,
		@Param("month") int month);

    @Select("""
            select EMOTION_ID,EMOTION_NAME,EMOTION_COLOR, DESCRIPTION as emotionDescription from EMOTION
            """)
    List<EmotionResponse> findAllToEmotionResponse();

}

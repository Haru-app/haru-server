package com.example.haruapp.emotion.mapper;

import com.example.haruapp.emotion.dto.EmotionQuestion;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface QuestionMapper {
    @Select("""
            select QUESTION_ID,DISPLAY_TEXT,EMOTION_ID
            from QUESTION
            """)
    List<EmotionQuestion> findByAllEmotionQuestion();
}

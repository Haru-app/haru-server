package com.example.haruapp.emotion.mapper;

import com.example.haruapp.emotion.dto.EmotionResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface EmotionMapper {

    @Select("""
            select EMOTION_ID,EMOTION_NAME from EMOTION;
            """)
    List<EmotionResponse> findAllToEmotionResponse();
}

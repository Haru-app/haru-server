package com.example.haruapp.emotion.dto;

import lombok.Data;

@Data
public class EmotionQuestion {
    private Long questionId;
    private String displayText;
    private Long emotionId;
}

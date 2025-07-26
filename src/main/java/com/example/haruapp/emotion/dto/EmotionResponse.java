package com.example.haruapp.emotion.dto;

import lombok.Data;

@Data
public class EmotionResponse {
    private Long emotionId;
    private String emotionName;
    private String emotionColor;
    private String emotionDescription;
}

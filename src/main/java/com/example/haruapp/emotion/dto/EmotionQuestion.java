package com.example.haruapp.emotion.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class EmotionQuestion {
    private Long questionId;
    private String displayText;
    private Long emotionId;

    @JsonProperty("formattedId")
    public String getFormattedId() {
        return "Q" + questionId;
    }
}

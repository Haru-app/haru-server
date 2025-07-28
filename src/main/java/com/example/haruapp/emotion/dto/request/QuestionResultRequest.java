package com.example.haruapp.emotion.dto.request;

import lombok.Data;

@Data
public class QuestionResultRequest {
    private String questionId;
    private int score;
}

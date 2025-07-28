package com.example.haruapp.emotion.dto.request;

import lombok.Data;

@Data
public class EmotionCardRequest {

	private Long userId;

	private Long courseId;

	private String comment;
}

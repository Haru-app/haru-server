package com.example.haruapp.emotion.dto.response;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EmotionHistoryResponse {

	private Long emotionId;

	private String emotionName;

	private Date createdAt;
}

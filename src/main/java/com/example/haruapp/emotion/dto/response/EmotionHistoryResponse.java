package com.example.haruapp.emotion.dto.response;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EmotionHistoryResponse {

	private Long emotionId;

	private String emotionName;

	private LocalDateTime createdAt;
}

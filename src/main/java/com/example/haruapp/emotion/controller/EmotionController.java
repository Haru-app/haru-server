package com.example.haruapp.emotion.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.haruapp.emotion.dto.response.EmotionHistoryResponse;
import com.example.haruapp.emotion.service.EmotionService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/emotion")
public class EmotionController {

	private final EmotionService emotionService;

	@GetMapping("/list")
	public ResponseEntity<List<EmotionHistoryResponse>> getEmotionHistory(
		@RequestParam("userId") Long userId,
		@RequestParam int year,
		@RequestParam int month
	) {

		List<EmotionHistoryResponse> result = emotionService.getEmotionsByUserIdAndDate(userId, year, month);
		return ResponseEntity.ok(result);
	}
}

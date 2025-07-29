package com.example.haruapp.emotion.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.haruapp.emotion.dto.response.EmotionCardResponse;
import com.example.haruapp.emotion.dto.response.EmotionCardUrlResponse;
import com.example.haruapp.emotion.service.EmotionCardService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/emotion")
@RequiredArgsConstructor
public class EmotionCardController {

	private final EmotionCardService emotionCardService;

	@PostMapping("/save")
	public ResponseEntity<EmotionCardResponse> saveEmotion(
		@RequestParam("comment") String comment,
		@RequestParam("userId") Long userId,
		@RequestParam("courseId") Long courseId,
		@RequestPart(value = "image", required = false) MultipartFile image
	) throws IOException {

		String aiImageUrl = emotionCardService.saveEmotion(userId, courseId, comment, image);
		return ResponseEntity.status(HttpStatus.CREATED)
			.body(new EmotionCardResponse(true, "감정 정보가 저장되었습니다.", aiImageUrl));
	}

	@GetMapping("/card-urls")
	public ResponseEntity<List<EmotionCardUrlResponse>> getEmotionCardUrlsByDate(
		@RequestParam Long userId,
		@RequestParam String date
	) {

		List<EmotionCardUrlResponse> urls = emotionCardService.getEmotionCardUrlsByDate(userId, date);
		return ResponseEntity.ok(urls);
	}

}
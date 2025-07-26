package com.example.haruapp.emotion.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.haruapp.emotion.dto.response.EmotionCardResponse;
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
	) {

		try {
			String aiImageUrl = emotionCardService.saveEmotion(userId, courseId, comment, image);
			return ResponseEntity.status(HttpStatus.CREATED)
				.body(new EmotionCardResponse(true, "감정 정보가 저장되었습니다.", aiImageUrl));
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
				.body(new EmotionCardResponse(false, e.getMessage(), null));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
				.body(new EmotionCardResponse(false, "서버 오류 발생", null));
		}
	}
}
package com.example.haruapp.emotion.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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

	@PostMapping(value = "/save", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<EmotionCardResponse> saveEmotion(
		@RequestParam("comment") String comment,
		@RequestParam("userId") Long userId,
		@RequestParam("courseId") Long courseId,
		@RequestPart(value = "image", required = false) MultipartFile image
	) {

		if (comment == null || comment.trim().isEmpty()) {
			return ResponseEntity.badRequest()
				.body(new EmotionCardResponse(false, "코멘트는 필수 입력 항목입니다."));
		}
		try {
			emotionCardService.saveEmotion(userId, courseId, comment, image);
			return ResponseEntity.status(HttpStatus.CREATED)
				.body(new EmotionCardResponse(true, "감정 정보가 저장되었습니다."));
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
				.body(new EmotionCardResponse(false, e.getMessage()));
		}
	}
}

package com.example.haruapp.emotion.service;

import java.io.IOException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.example.haruapp.emotion.mapper.EmotionCardMapper;
import com.example.haruapp.util.GcpStorageUtil;
import com.example.haruapp.util.OpenAiImageService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmotionCardService {

	private final EmotionCardMapper emotionCardMapper;

	private final GcpStorageUtil gcpStorageUtil;

	private final OpenAiImageService openAiImageService;

	@Transactional
	public String saveEmotion(Long userId, Long courseId, String comment, MultipartFile image) throws IOException {

		String prompt = "In the style of a Ghibli animation, show people shopping in front of a modern department store on a sunny day. Blue sky, fluffy clouds, green trees, warm light, cheerful characters.";

		String imageUrl = openAiImageService.generateImage(prompt);

		emotionCardMapper.insertEmotionCard(userId, courseId, imageUrl);
		return imageUrl;
	}
}

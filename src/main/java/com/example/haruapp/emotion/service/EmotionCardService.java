package com.example.haruapp.emotion.service;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.List;

import javax.imageio.ImageIO;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.example.haruapp.emotion.dto.response.EmotionCardUrlResponse;
import com.example.haruapp.emotion.mapper.EmotionCardMapper;
import com.example.haruapp.global.error.CustomException;
import com.example.haruapp.global.error.ErrorCode;
import com.example.haruapp.util.GcpStorageUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmotionCardService {

	private final EmotionCardMapper emotionCardMapper;

	private final GcpStorageUtil gcpStorageUtil;

	@Transactional
	public String saveEmotion(Long userId, Long courseId, String comment, MultipartFile image) {

		try {
			// 인코딩
			String base64;
			try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
				BufferedImage bufferedImage = ImageIO.read(image.getInputStream());
				ImageIO.write(bufferedImage, "jpg", baos);
				base64 = Base64.getEncoder().encodeToString(baos.toByteArray());
			}

			// GCP 업로드
			String imageUrl = gcpStorageUtil.uploadBase64Image(base64);

			emotionCardMapper.insertEmotionCard(userId, courseId, imageUrl);

			// URL
			return imageUrl;

		} catch (IOException e) {
			throw new CustomException(ErrorCode.IMAGE_PROCESSING_FAILED);
		}
	}

	public List<EmotionCardUrlResponse> getEmotionCardUrlsByDate(Long userId, String date) {

		return emotionCardMapper.findEmotionCardUrlsByDate(userId, date);
	}

	public boolean existsEmotionCard(Long courseId) {

		return emotionCardMapper.existsByCourseId(courseId);
	}
}


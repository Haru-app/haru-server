package com.example.haruapp.emotion.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

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
	public void saveEmotion(Long userId, Long courseId, String comment, MultipartFile image) {

		String filePath = null;

		if (image != null && !image.isEmpty()) {
			String contentType = image.getContentType();
			if (contentType == null ||
				!(contentType.equals("image/jpeg") || contentType.equals("image/png") || contentType.equals(
					"image/jpg"))) {
				throw new CustomException(ErrorCode.UNSUPPORTED_IMAGE_FORMAT);
			}
			filePath = gcpStorageUtil.upload(image);
		}
		emotionCardMapper.insertEmotionCard(userId, courseId, filePath);
	}
}

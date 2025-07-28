package com.example.haruapp.emotion.service;

import java.awt.*;
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
	public String saveEmotion(Long userId, Long courseId, String comment, MultipartFile image)  {
		try {
			BufferedImage frame = ImageIO.read(getClass().getResourceAsStream("/frame/sunny.png"));
			BufferedImage userImage;

			if (image != null && !image.isEmpty()) {
				userImage = ImageIO.read(image.getInputStream());
			} else {
				userImage = ImageIO.read(getClass().getResourceAsStream("/frame/default.png"));
			}

			int frameWidth = frame.getWidth();
			int frameHeight = frame.getHeight();
			int userImgWidth = 900;
			int userImgHeight = 500;
			int userX = (frameWidth - userImgWidth) / 2;
			int userY = 100;
			int commentY = frameHeight - 100;

			BufferedImage combined = new BufferedImage(frameWidth, frameHeight, BufferedImage.TYPE_INT_ARGB);
			Graphics2D g = combined.createGraphics();
			g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
			g.drawImage(frame, 0, 0, null);
			g.drawImage(userImage.getScaledInstance(userImgWidth, userImgHeight, Image.SCALE_SMOOTH), userX, userY,
				null);

			g.setColor(Color.BLACK);
			g.setFont(new Font("SansSerif", Font.PLAIN, 32));
			FontMetrics fm = g.getFontMetrics();
			int textWidth = fm.stringWidth(comment);
			g.drawString(comment, (frameWidth - textWidth) / 2, commentY);
			g.dispose();

			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ImageIO.write(combined, "png", baos);
			String base64 = Base64.getEncoder().encodeToString(baos.toByteArray());

			String imageUrl = gcpStorageUtil.uploadBase64Image(base64);
			emotionCardMapper.insertEmotionCard(userId, courseId, imageUrl);
			return imageUrl;
		} catch(IOException e) {
			throw new CustomException(ErrorCode.IMAGE_PROCESSING_FAILED);
		}
	}

	public List<EmotionCardUrlResponse> getEmotionCardUrls(Long userId, Long courseId) {

		return emotionCardMapper.findEmotionCardUrls(userId, courseId);
	}
}


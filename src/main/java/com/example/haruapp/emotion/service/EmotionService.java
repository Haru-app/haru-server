package com.example.haruapp.emotion.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.haruapp.emotion.dto.response.EmotionHistoryResponse;
import com.example.haruapp.emotion.mapper.EmotionMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmotionService {

	private final EmotionMapper emotionMapper;

	public List<EmotionHistoryResponse> getEmotionsByUserIdAndDate(Long userId, int year, int month) {

		return emotionMapper.findEmotionsByUserIdAndMonth(userId, year, month);
	}
}

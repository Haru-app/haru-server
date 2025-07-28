package com.example.haruapp.emotion.service;

import com.example.haruapp.emotion.dto.EmotionResponse;
import com.example.haruapp.emotion.mapper.EmotionMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import com.example.haruapp.emotion.dto.response.EmotionHistoryResponse;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmotionService {

    private final EmotionMapper emotionMapper;

    public List<EmotionResponse> getAllEmotionResponse() {

        return emotionMapper.findAllToEmotionResponse();
    }
  
    public List<EmotionHistoryResponse> getEmotionsByUserIdAndDate(Long userId, int year, int month) {

      return emotionMapper.findEmotionsByUserIdAndMonth(userId, year, month);
    }
}

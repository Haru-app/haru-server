package com.example.haruapp.emotion.controller;

import com.example.haruapp.emotion.dto.EmotionQuestion;
import com.example.haruapp.emotion.dto.EmotionResponse;
import com.example.haruapp.emotion.dto.request.QuestionResultRequest;
import com.example.haruapp.emotion.dto.response.EmotionHistoryResponse;
import com.example.haruapp.emotion.dto.response.EmotionTopResponse;
import com.example.haruapp.emotion.service.EmotionService;
import com.example.haruapp.emotion.service.EmotionSimilarityService;
import com.example.haruapp.emotion.service.QuestionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/api")
@RequiredArgsConstructor
public class EmotionController {

    private final EmotionSimilarityService emotionSimilarityService;
    private final QuestionService questionService;
    private final EmotionService emotionService;
  
    @GetMapping("/emotion/list")
    public ResponseEntity<List<EmotionHistoryResponse>> getEmotionHistory(
      @RequestParam("userId") Long userId,
      @RequestParam int year,
      @RequestParam int month
    ) {

      List<EmotionHistoryResponse> result = emotionService.getEmotionsByUserIdAndDate(userId, year, month);
      return ResponseEntity.ok(result);
    }

    @GetMapping("/emotions")
    public ResponseEntity<List<EmotionResponse>> getEmotionQuestions() {

        List<EmotionResponse> emotionList = emotionService.getAllEmotionResponse();

        return ResponseEntity.ok(emotionList);
    }

    @GetMapping("/emotion/random/question")
    public ResponseEntity<?> getRandomQuestion() {

        int count = 4;

        List<EmotionQuestion> questions = questionService.getRandomQuestionByEmotion();

        log.info("questions {}", questions);

        return ResponseEntity.ok(questions);
    }

    @PostMapping("/emotions/similarity")
    public ResponseEntity<?> embeddingEmotion(@RequestBody List<QuestionResultRequest> questionResultRequestList) {

        Map<String, Double> result = emotionSimilarityService.calculateSimilarity(questionResultRequestList);

        String emotionTop1 = emotionSimilarityService.getTop1(result);

        log.info("result {}", result);

        return ResponseEntity.ok(EmotionTopResponse
                .builder()
                .emotion(emotionTop1)
                .build());
    }
}

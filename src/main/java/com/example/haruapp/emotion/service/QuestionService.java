package com.example.haruapp.emotion.service;

import com.example.haruapp.emotion.dto.EmotionQuestion;
import com.example.haruapp.emotion.mapper.QuestionMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class QuestionService {

    private final QuestionMapper questionMapper;

    // todo 이후 질문이 너무 몰아 나오면 수정 예정
    public List<EmotionQuestion> getRandomQuestion(int count) {

        List<EmotionQuestion> emotionQuestions = questionMapper.findByAllEmotionQuestion();

        Collections.shuffle(emotionQuestions);  // 리스트 섞기

        return emotionQuestions.stream()
                .limit(count)
                .toList();
    }
}

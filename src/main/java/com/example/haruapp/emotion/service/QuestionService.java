package com.example.haruapp.emotion.service;

import com.example.haruapp.emotion.dto.EmotionQuestion;
import com.example.haruapp.emotion.mapper.QuestionMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class QuestionService {

    private final QuestionMapper questionMapper;

    // todo 이후 질문이 너무 몰아 나오면 수정 예정
    public List<EmotionQuestion> getRandomQuestionByEmotion(int count) {
        List<EmotionQuestion> emotionQuestions = questionMapper.findByAllEmotionQuestion();

        // emotionId 기준으로 그룹화
        Map<Long, List<EmotionQuestion>> grouped = emotionQuestions.stream()
                .collect(Collectors.groupingBy(EmotionQuestion::getEmotionId));

        // 각 emotionId 그룹에서 랜덤하게 1개씩 선택
        List<EmotionQuestion> selected = new ArrayList<>();
        Random random = new Random();
        for (List<EmotionQuestion> group : grouped.values()) {
            if (!group.isEmpty()) {
                EmotionQuestion randomQuestion = group.get(random.nextInt(group.size()));
                selected.add(randomQuestion);
            }
        }

        // 선택된 질문 중에서 count개만 랜덤하게 반환
        Collections.shuffle(selected);
        return selected.stream()
                .limit(count)
                .collect(Collectors.toList());
    }
}

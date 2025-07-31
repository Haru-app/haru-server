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

        // emotionId 기준 그룹화
        Map<Long, List<EmotionQuestion>> grouped = emotionQuestions.stream()
                .collect(Collectors.groupingBy(EmotionQuestion::getEmotionId));

        List<EmotionQuestion> selected = new ArrayList<>();
        Random random = new Random();

        // 1. emotionId 5와 6에서 각각 1개씩 무조건 포함
        List<Long> mustIncludeIds = List.of(5L, 6L);
        for (Long id : mustIncludeIds) {
            List<EmotionQuestion> group = grouped.get(id);
            if (group != null && !group.isEmpty()) {
                selected.add(group.get(random.nextInt(group.size())));
            }
        }

        // 2. 나머지 emotionId 중에서 랜덤하게 2개 추출
        List<Long> otherEmotionIds = grouped.keySet().stream()
                .filter(id -> !mustIncludeIds.contains(id))
                .collect(Collectors.toList());

        Collections.shuffle(otherEmotionIds); // 랜덤 순서
        for (int i = 0; i < 2 && i < otherEmotionIds.size(); i++) {
            Long emotionId = otherEmotionIds.get(i);
            List<EmotionQuestion> group = grouped.get(emotionId);
            if (group != null && !group.isEmpty()) {
                selected.add(group.get(random.nextInt(group.size())));
            }
        }

        return selected;
    }

}

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
    public List<EmotionQuestion> getRandomQuestionByEmotion() {
        List<EmotionQuestion> emotionQuestions = questionMapper.findByAllEmotionQuestion();
        log.info("🔍 전체 질문 개수: {}", emotionQuestions.size());

        // emotionId 기준 그룹화
        Map<Long, List<EmotionQuestion>> grouped = emotionQuestions.stream()
                .collect(Collectors.groupingBy(EmotionQuestion::getEmotionId));

        log.info("📊 감정별 그룹 수: {}", grouped.size());

        List<EmotionQuestion> selected = new ArrayList<>();
        Random random = new Random();

        // 1. emotionId 5, 6에서 각각 2개씩 무조건 포함
        List<Long> mustIncludeIds = List.of(5L, 6L);
        for (Long id : mustIncludeIds) {
            List<EmotionQuestion> group = grouped.get(id);
            log.info("🧩 필수 포함 감정 ID {}의 질문 수: {}", id, group != null ? group.size() : 0);

            if (group != null && group.size() >= 2) {
                Collections.shuffle(group);
                selected.addAll(group.subList(0, 2));
                log.info("✅ 감정 ID {}에서 2개 선택", id);
            } else if (group != null) {
                selected.addAll(group); // 2개 미만일 경우 가능한 만큼 추가
                log.warn("⚠ 감정 ID {}에서 2개 미만이므로 {}개만 선택", id, group.size());
            }
        }

        // 2. 나머지 emotionId 중에서 랜덤하게 6개 추출
        List<Long> otherEmotionIds = grouped.keySet().stream()
                .filter(id -> !mustIncludeIds.contains(id))
                .collect(Collectors.toList());

        Collections.shuffle(otherEmotionIds);
        int remainingCount = 4;

        for (Long emotionId : otherEmotionIds) {
            if (remainingCount == 0) break;

            List<EmotionQuestion> group = grouped.get(emotionId);
            if (group != null && !group.isEmpty()) {
                EmotionQuestion selectedQuestion = group.get(random.nextInt(group.size()));
                selected.add(selectedQuestion);
                log.info("🎲 감정 ID {}에서 1개 랜덤 선택: {}", emotionId, selectedQuestion.getEmotionId());
                remainingCount--;
            }
        }

        // 3. 부족한 경우 전체에서 추가 보정
        if (selected.size() < 10) {
            List<EmotionQuestion> remainingPool = new ArrayList<>(emotionQuestions);
            remainingPool.removeAll(selected);
            Collections.shuffle(remainingPool);
            int gap = 10 - selected.size();
            for (int i = 0; i < gap && i < remainingPool.size(); i++) {
                selected.add(remainingPool.get(i));
                log.warn("🔧 보정 질문 추가: {}", remainingPool.get(i).getDisplayText());
            }
        }

        // 4. 최종적으로 셔플
        Collections.shuffle(selected);
        log.info("✅ 최종 선택된 질문 수: {}", selected.size());
        selected.forEach(q -> log.debug("📝 최종 질문: {} (emotionId: {})", q.getDisplayText(), q.getEmotionId()));

        return selected;
    }
}

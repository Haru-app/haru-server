package com.example.haruapp.emotion.service;

import com.example.haruapp.emotion.dto.request.QuestionResultRequest;
import com.example.haruapp.emotion.util.ParsingUtil;
import com.example.haruapp.emotion.util.PythonClient;
import com.example.haruapp.global.repository.RedisRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmotionSimilarityService {

    private static final String similarityRedisJsonKey = "question_emotion_similarity.json";
    private final RedisRepository redisRepository;

//    private static final Map<String, List<String>> OPPOSITE_EMOTION_MAP = Map.of(
//            "기쁨", List.of("무기력", "스트레스"),
//            "여유", List.of("스트레스"),
//            "설렘", List.of("무기력"),
//            "호기심", List.of("무기력", "스트레스"),
//            "무기력", List.of("활기참"),
//            "스트레스", List.of("여유"),
//            "활기참", List.of("무기력")
//    );



    public Map<String, Double> calculateSimilarity(List<QuestionResultRequest> questionResultRequestList) {

        String beforeParse = redisRepository.getSimilarityMap(similarityRedisJsonKey);

//        log.info("before parse: {}", beforeParse);

        if (beforeParse == null || beforeParse.isEmpty()) {

            log.warn("Redis에 유사도 맵이 없음. Python 서버에 요청합니다.");

            PythonClient.callingPythonServer();
        }

        beforeParse = redisRepository.getSimilarityMap(similarityRedisJsonKey);

        Map<String, Map<String, Double>> similarityMap = ParsingUtil.parseSim(beforeParse);

        log.info("similarityMap: {}", similarityMap);

        return calculateScore(similarityMap, questionResultRequestList);
    }
    public Map<String, Double> calculateScore(
            Map<String, Map<String, Double>> similarityMap,
            List<QuestionResultRequest> questionResultRequestList) {

        Map<String, Double> emotionScoreMap = new HashMap<>();

        for (QuestionResultRequest questionResult : questionResultRequestList) {
            String questionId = questionResult.getQuestionId();
            int userScore = questionResult.getScore();

            Map<String, Double> emotionSimMap = similarityMap.get(questionId);
            log.info("emotionSimMap: {}", emotionSimMap);

            double userWeight = switch (userScore) {
                case 10 -> 2.0;
                case 5 -> 0.5;
                case 1 -> -1.5;
                default -> 0.0;
            };

            for (Map.Entry<String, Double> entry : emotionSimMap.entrySet()) {
                String emotion = entry.getKey();
                double similarity = entry.getValue();

                double weightedScore = similarity * userWeight;
                emotionScoreMap.put(emotion,
                        emotionScoreMap.getOrDefault(emotion, 0.0) + weightedScore);
//
//                // 반대 감정 보정: '예' → 반대 감정 감점 / '아니오' → 반대 감정 가점
//                List<String> opposites = OPPOSITE_EMOTION_MAP.getOrDefault(emotion, List.of());
//                for (String opposite : opposites) {
//                    double delta = switch (userScore) {
//                        case 10 -> -3.0;
//                        case 1 -> +3.0;
//                        default -> 0.0;
//                    };
//                    if (delta != 0.0) {
//                        double updated = emotionScoreMap.getOrDefault(opposite, 0.0) + delta;
//                        emotionScoreMap.put(opposite, updated);
//                    }
//                }
            }
        }

        return emotionScoreMap;
    }

    public String getTop1(Map<String, Double> result) {

        return result.entrySet().stream()

                .sorted((a, b) ->

                        Double.compare(b.getValue(), a.getValue())) // value 기준 내림차순 정렬

                .limit(1) // 상위 count개만

                .map(Map.Entry::getKey) // 감정명(key)만 추출

                .toList().getFirst(); // 리스트로 반환
    }
}

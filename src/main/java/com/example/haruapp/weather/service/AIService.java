package com.example.haruapp.weather.service;

import com.example.haruapp.weather.dto.WeatherForecastDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.ChatClient.CallResponseSpec;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AIService {
    private final ChatClient chatClient;

    public AIService(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }

    public String generation(WeatherForecastDto dto) {
        String userInput = dto.toString() +
                "위 데이터를 보고 아래 카테고리 5개 중 하나 선택하세요\n"+
                "다음 중 날씨 카테고리 5개 중 하나를 선택하세요:\n" +
                "1. 맑음\n" +
                "2. 비\n" +
                "3. 눈\n" +
                "4. 흐림\n" +
                "5. 천둥\n\n" +
                "**반드시 아래 형식으로** 응답해 주세요:\n\n" +
                "**[중요] 형식: '카테고리단어' → 예: '맑음'**\n" +
                "형식 이외의 설명이나 텍스트 없이 정확히 '단어' 형태만 출력해 주세요.";

        log.info("입력값: "+userInput);

        CallResponseSpec callResponseSpec= this.chatClient.prompt()
                .user(userInput)
                .call();

        ChatResponse response = callResponseSpec.chatResponse();
        log.info("리스폰스"+response.toString());
        String content= callResponseSpec.content();
        log.info("컨텐츠"+content);

        return content;
    }
}
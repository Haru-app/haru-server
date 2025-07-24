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
        String userInput = dto.toString() +"날씨 카테고리 5개\n" +
                "1. 맑음\n" +
                "2. 비\n" +
                "3. 눈\n" +
                "4. 흐림\n" +
                "5. 천둥 중에 하나 단어 선택해고," +
                "그리고 추가적으로 날씨 1단어 추가해서 2단어 줘";
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
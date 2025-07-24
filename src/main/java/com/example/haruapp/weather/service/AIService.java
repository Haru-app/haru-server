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
        String userInput = dto.toString() +"이거 바탕으로 날씨 2단어 로 만들어줘";
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
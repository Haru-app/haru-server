package com.example.haruapp.weather.config;

import com.example.haruapp.global.repository.RedisRepository;
import com.example.haruapp.weather.service.AIService;
import com.example.haruapp.weather.service.WeatherApiClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class WeatherConfig {

    @Value("${weather.key}")
    private String weatherKey;

    @Bean
    public WeatherApiClient weatherApiClient(AIService aiService, RedisRepository redisRepository) {
        RestTemplate restTemplate = new RestTemplate();

        return new WeatherApiClient(restTemplate, weatherKey,aiService,redisRepository);
    }
}

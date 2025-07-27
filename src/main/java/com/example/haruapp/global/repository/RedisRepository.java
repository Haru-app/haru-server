package com.example.haruapp.global.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
@Slf4j
@RequiredArgsConstructor
public class RedisRepository {

    private final StringRedisTemplate redisTemplate;

    public String getWeather(String baseDate, String baseTime){
        return redisTemplate.opsForValue().get(baseDate + baseTime);
    }

    public void setWeather(String baseDate,String baseTime,String weather) {
        redisTemplate.opsForValue().set(baseDate + baseTime, weather, Duration.ofMinutes(30));
    }

    public String getSimilarityMap(String key) {
        return redisTemplate.opsForValue().get(key);
    }
}

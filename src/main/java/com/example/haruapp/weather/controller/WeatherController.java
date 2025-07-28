package com.example.haruapp.weather.controller;

import com.example.haruapp.weather.dto.WeatherResponse;
import com.example.haruapp.weather.service.WeatherApiClient;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class WeatherController {

    private final WeatherApiClient weatherApiClient;

    @GetMapping("/weather")
    public ResponseEntity<?> weather() {
        String weatherSummary = weatherApiClient.getUltraShortForecast();

        return ResponseEntity.ok(WeatherResponse.builder()
                .weather(weatherSummary)
                .build());
    }
}

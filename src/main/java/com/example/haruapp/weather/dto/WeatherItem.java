package com.example.haruapp.weather.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WeatherItem {
    private String category;
    private String label;
    private String value;

    public static WeatherItem of(String category, String label, String value) {
        return WeatherItem.builder()
                .category(category)
                .label(label)
                .value(value)
                .build();
    }
}
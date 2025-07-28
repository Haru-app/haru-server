package com.example.haruapp.weather.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WeatherForecastDto {
    private String forecastTime;
    private int locationNx;
    private int locationNy;

    private List<WeatherItem> weather;

    public static WeatherForecastDto of(String forecastTime, int locationNx, int locationNy, List<WeatherItem> weather) {
        return WeatherForecastDto.builder()
                .forecastTime(forecastTime)
                .weather(weather)
                .locationNx(locationNx)
                .locationNy(locationNy)
                .build();
    }
}

package com.example.haruapp.weather.dto;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class WeatherResponse {
    private String weather;

}

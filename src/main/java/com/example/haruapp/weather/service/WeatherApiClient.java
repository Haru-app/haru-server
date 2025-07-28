package com.example.haruapp.weather.service;

import com.example.haruapp.global.error.CustomException;
import com.example.haruapp.global.error.ErrorCode;
import com.example.haruapp.global.repository.RedisRepository;
import com.example.haruapp.weather.dto.WeatherForecastDto;
import com.example.haruapp.weather.util.DateFormatUtil;
import com.example.haruapp.weather.util.WeatherXMLCleaningUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.client.RestTemplate;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import java.net.URI;

@Slf4j
@RequiredArgsConstructor
public class WeatherApiClient {

    private final RestTemplate restTemplate;
    private final String serviceKey;
    private final AIService aiService;
    private final RedisRepository redisRepository;

    public String getUltraShortForecast() {
        String baseDate = DateFormatUtil.getBaseDate();  // "yyyyMMdd"
        String baseTime = DateFormatUtil.getBaseTime();  // "HHmm"


        String weather =redisRepository.getWeather(baseDate,baseTime);

        if(weather != null)
            return weather;

        String weatherResultXML = getWeatherResultXML(baseDate,baseTime);

        WeatherForecastDto weatherForecastDto= parseForecastToDto(weatherResultXML);
        log.info("날씨 DTO: {}", weatherForecastDto);

        String weatherSummary =aiService.generation(weatherForecastDto);
        log.info("날씨 요약: {}", weatherSummary);

        redisRepository.setWeather(baseDate,baseTime,weatherSummary);

        return weatherSummary;
    }

    public String getWeatherResultXML(String baseDate, String baseTime) {
        String url = String.format(WeatherXMLCleaningUtil.BASE_URL, serviceKey, baseDate, baseTime, WeatherXMLCleaningUtil.NX, WeatherXMLCleaningUtil.NY);

        try {
            return restTemplate.getForObject(URI.create(url), String.class);
        } catch (Exception e) {
            throw new CustomException(ErrorCode.WEATHERAPI_SERVER_ERROR);
        }
    }

    public WeatherForecastDto parseForecastToDto(String xml) {

        Document doc = WeatherXMLCleaningUtil.parseXml(xml);
        doc.getDocumentElement().normalize();

        NodeList itemList = doc.getElementsByTagName("item");
        return WeatherXMLCleaningUtil.dataCleaning(itemList);
    }
}

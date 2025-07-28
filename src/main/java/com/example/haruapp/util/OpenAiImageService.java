package com.example.haruapp.util;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class OpenAiImageService {

	private final RestTemplate restTemplate = new RestTemplate();

	@Value("${openai.api.key}")
	private String apiKey;

	public String generateImage(String prompt) {

		String url = "https://api.openai.com/v1/images/generations";

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.setBearerAuth(apiKey);

		Map<String, Object> body = Map.of(
			"prompt", prompt,
			"n", 1,
			"size", "1024x1024"
		);

		HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);
		ResponseEntity<Map> response = restTemplate.postForEntity(url, entity, Map.class);

		List<Map<String, String>> data = (List<Map<String, String>>) response.getBody().get("data");
		return data.get(0).get("url"); // 이미지 URL 반환
	}
}


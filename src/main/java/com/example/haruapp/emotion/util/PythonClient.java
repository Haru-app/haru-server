package com.example.haruapp.emotion.util;

import com.example.haruapp.global.error.CustomException;
import com.example.haruapp.global.error.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;


@Slf4j
public class PythonClient {

//    public static final String PYTHON_API_URL = "http://3.35.210.83:8000/question/similarity-map";
    public static final String PYTHON_API_URL = "http://localhost:8000/question/similarity-map";
    private static final RestTemplate restTemplate = new RestTemplate();

    public static void callingPythonServer() {
        try {

            ResponseEntity<String> response = restTemplate.getForEntity(PYTHON_API_URL, String.class);

            if (!response.getStatusCode().is2xxSuccessful()) {
                throw new CustomException(ErrorCode.SIMILARITY_FETCH_FAILED);
            }
            log.info("✅ Python 서버 응답 확인됨 (200 OK)");
        } catch (Exception e) {
            log.error("❗ Python 서버 요청 중 예외 발생", e);
            throw new CustomException(ErrorCode.SIMILARITY_FETCH_FAILED);
        }
    }
}

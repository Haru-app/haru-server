package com.example.haruapp.subscription.external;

import com.example.haruapp.global.error.CustomException;
import com.example.haruapp.global.error.ErrorCode;
import com.example.haruapp.subscription.dto.response.BillingResponse;
import com.example.haruapp.subscription.dto.response.PaymentResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class TossPaymentsClient {

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${toss.secret-key}")
    private String secretKey;

    public BillingResponse requestBillingKey(String authKey, String customerKey) {
        String credentials = secretKey + ":";
        String encoded = Base64.getEncoder().encodeToString(credentials.getBytes(StandardCharsets.UTF_8));

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Basic " + encoded);
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, String> body = new HashMap<>();
        body.put("customerKey", customerKey); // 반드시 있어야 함!

        HttpEntity<Map<String, String>> request = new HttpEntity<>(body, headers);
        String url = "https://api.tosspayments.com/v1/billing/authorizations/" + authKey;

        ResponseEntity<BillingResponse> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                request,
                BillingResponse.class
        );
        return response.getBody();
    }

    public PaymentResponse requestAutoPayment(String billingKey, String customerKey) {
        String credentials = secretKey + ":";
        String encoded = Base64.getEncoder().encodeToString(credentials.getBytes(StandardCharsets.UTF_8));

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Basic " + encoded);
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> body = new HashMap<>();
        body.put("amount", 2900);    // 고정 금액
        body.put("customerKey", customerKey);
        body.put("orderId", "order-" + UUID.randomUUID()); // UUID 기반
        body.put("orderName", "HaRU 감정 카드 구독권 1개월");

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);
        String url = "https://api.tosspayments.com/v1/billing/" + billingKey;

        try {
            ResponseEntity<PaymentResponse> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    request,
                    PaymentResponse.class
            );
            return response.getBody();
        } catch (HttpClientErrorException | HttpServerErrorException ex) {    // Toss 응답 예외 처리 (4xx, 5xx)
            log.error("자동결제 요청 실패 - Toss 응답 오류: {}", ex.getResponseBodyAsString(), ex);
            throw new CustomException(ErrorCode.TOSS_PAYMENT_FAILED);
        } catch (RestClientException ex) {    // 기타 통신 예외 처리
            log.error("자동결제 요청 실패 - RestClientException: {}", ex.getMessage(), ex);
            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

}

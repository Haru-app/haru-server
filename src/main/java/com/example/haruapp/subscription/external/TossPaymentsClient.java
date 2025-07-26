package com.example.haruapp.subscription.external;

import com.example.haruapp.subscription.dto.response.BillingResponse;
import com.example.haruapp.subscription.dto.response.PaymentResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

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
        String encodedBillingKey = URLEncoder.encode(billingKey, StandardCharsets.UTF_8);
        String url = "https://api.tosspayments.com/v1/billing/" + billingKey;

        ResponseEntity<PaymentResponse> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                request,
                PaymentResponse.class
        );
        return response.getBody();
    }

}

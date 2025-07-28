package com.example.haruapp.subscription.controller;

import com.example.haruapp.subscription.dto.request.FcmTokenRequest;
import com.example.haruapp.subscription.service.FcmService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/subscriptions")
@RequiredArgsConstructor
public class FcmController {

    private final FcmService fcmService;

    @PostMapping("/fcm-token")
    public ResponseEntity<Void> saveFcmToken(@RequestBody FcmTokenRequest request) {
        try {
            fcmService.saveFcmToken(request.getUserId(), request.getFcmToken());
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            log.warn("잘못된 FCM 토큰 요청: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

}

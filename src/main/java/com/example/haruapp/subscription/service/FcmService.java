package com.example.haruapp.subscription.service;

import com.example.haruapp.subscription.mapper.FcmTokenMapper;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class FcmService {

    private final FcmTokenMapper fcmTokenMapper;

    public void saveFcmToken(Long userId, String token) {
        if (token == null || token.isBlank()) {
            throw new IllegalArgumentException("FCM 토큰은 비어 있을 수 없습니다.");
        }
        fcmTokenMapper.saveFcmToken(userId, token);
        log.info("수신된 FCM 토큰: {}", token);
    }

    public void sendNotification(Long userId, String title, String body) {
        String token = fcmTokenMapper.findFcmTokenByUserId(userId);

        if (token == null || token.isBlank()) {
            throw new IllegalArgumentException("FCM 토큰은 비어 있을 수 없습니다.");
        }

        Notification notification = Notification.builder()
                .setTitle(title)
                .setBody(body)
                .build();

        Message message = Message.builder()
                .setToken(token)
                .setNotification(notification)
                .build();

        try {
            String response = FirebaseMessaging.getInstance().send(message);
            log.info("FCM 알림 전송 성공: {}", response);
        } catch (FirebaseMessagingException e) {
            log.error("FCM 알림 전송 실패", e);
        }
    }

}

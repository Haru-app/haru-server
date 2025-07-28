package com.example.haruapp.subscription.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FcmTokenRequest {

    private Long userId;
    private String fcmToken;

}

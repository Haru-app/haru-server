package com.example.haruapp.auth.domain;

import lombok.Data;

@Data
public class OauthRequest {
    private Long userId;
    private String oauthType; // "KAKAO"
    private String oauthId;
    private String email;
    private String nickname;
}

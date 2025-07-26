package com.example.haruapp.member.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Member {

    private Long userId;
    private String email;
    private String nickname;
    private String customerKey;

}
package com.example.haruapp.auth.domain;

import java.util.Date;

import lombok.Data;

@Data
public class Member {
	private Long userId;
	private String email;
	private String nickname;
	private String password;
	private Date createdAt;
}

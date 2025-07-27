package com.example.haruapp.auth.dto.request;

import lombok.Data;

@Data
public class SignupRequest {
	private String email;
	private String nickname;
	private String password;
}

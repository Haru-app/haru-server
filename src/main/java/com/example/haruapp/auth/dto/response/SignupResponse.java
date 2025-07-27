package com.example.haruapp.auth.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SignupResponse {
	private String email;
	private String nickname;
	private String message;
}

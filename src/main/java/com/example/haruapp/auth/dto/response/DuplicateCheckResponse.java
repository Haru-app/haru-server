package com.example.haruapp.auth.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DuplicateCheckResponse {
	private boolean isDuplicated;
	private String message;
}

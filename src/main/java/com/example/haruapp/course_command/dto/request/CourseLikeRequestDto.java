package com.example.haruapp.course_command.dto.request;

import lombok.Data;

@Data
public class CourseLikeRequestDto {
	private Long courseId;
	private Long userId;
}

package com.example.haruapp.course_command.dto.response;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CourseListResponse {

	private Long courseId;

	private String title;

	private String emotionName;

	private String weather;

	private Long userId;

	private String userNickname;

	private int totalStores;

	private List<String> storeNames;

	private int likeCount;

	private Boolean isLiked;
}

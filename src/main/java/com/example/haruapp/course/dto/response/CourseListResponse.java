package com.example.haruapp.course.dto.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CourseListResponse {

	private Long courseId;

	private String title;

	private String emotionName1;

	private String emotionName2;

	private String weather;

	private Long userId;

	private String userNickname;

	private int totalStores;

	private List<String> storeNames;

	private int likeCount;

	private boolean isLiked;
}

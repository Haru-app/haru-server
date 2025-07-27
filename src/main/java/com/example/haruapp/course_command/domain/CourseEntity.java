package com.example.haruapp.course_command.domain;

import lombok.Data;

@Data
public class CourseEntity {
    private Long courseId;
    private String title;
    private String weather;
    private Long userId;
    private Long emotionId;
}

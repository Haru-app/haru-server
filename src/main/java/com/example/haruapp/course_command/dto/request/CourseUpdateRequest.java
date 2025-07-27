package com.example.haruapp.course_command.dto.request;

import lombok.Data;

import java.util.List;

@Data
public class CourseUpdateRequest {
    private String title;
    private List<Long> storeList;
}

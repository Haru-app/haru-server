package com.example.haruapp.course_command.dto.request;

import lombok.Data;

import java.util.List;

@Data
public class CourseSaveRequest {
    private String title;
    private String weather;
    private Long emotionId;
    private List<Long> storeList;
}

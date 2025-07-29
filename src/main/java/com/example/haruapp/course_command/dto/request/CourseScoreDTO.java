package com.example.haruapp.course_command.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseScoreDTO {
    private Double averageScore;
    private Double maxPossibleScore;
    private Double diversityScore;
    private Double similarityStddev;
}

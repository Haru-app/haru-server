package com.example.haruapp.course_command.controller;

import com.example.haruapp.course_command.dto.request.CourseSaveRequest;
import com.example.haruapp.course_command.dto.request.CourseUpdateRequest;
import com.example.haruapp.course_command.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/course")
@RequiredArgsConstructor
public class CourseController {

    private final CourseService courseService;

    /**
     * 추천 코스 저장
     * */
    @PostMapping
    public ResponseEntity<Long> saveCourse(
            @RequestBody CourseSaveRequest request,
            @RequestParam("userId") Long userId
    ) {
        Long courseId = courseService.saveCourse(request, userId);
        return ResponseEntity.ok(courseId);
    }


    /**
     * 내 코스 수정
     * - 코스 제목, 매장과 매장 순서 수정 가능
     * */
    @PutMapping("/{courseId}")
    public ResponseEntity<?> updateCourse(
            @PathVariable Long courseId,
            @RequestParam("userId") Long userId,
            @RequestBody CourseUpdateRequest request
    ) {
        courseService.updateCourse(courseId, userId, request);
        return ResponseEntity.ok(courseId);
    }


    /**
     * 코스 스크랩
     * - 다른 사람의 코스를 복사해서, 내 코스로 새로 생성
     * */
    @PostMapping("/{courseId}/scrap")
    public ResponseEntity<Long> scrapCourse(
            @PathVariable Long courseId,
            @RequestParam("userId") Long userId
    ) {
        Long newCourseId = courseService.scrapCourse(courseId, userId);
        return ResponseEntity.ok(newCourseId);
    }
}


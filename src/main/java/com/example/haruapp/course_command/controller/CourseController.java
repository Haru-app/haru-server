package com.example.haruapp.course_command.controller;

import java.util.List;

import com.example.haruapp.course_command.dto.request.CourseLikeRequestDto;
import com.example.haruapp.course_command.dto.response.CourseListResponse;
import com.example.haruapp.course_command.dto.request.CourseSaveRequest;
import com.example.haruapp.course_command.dto.request.CourseUpdateRequest;
import com.example.haruapp.course_command.service.CourseService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
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

    /**
     * 코스 목록 인기순 조회
     * */
    @GetMapping("/popular")
    public ResponseEntity<List<CourseListResponse>> getCoursesSortedByPopularity(@RequestParam("userId") Long userId) {

        List<CourseListResponse> courses = courseService.getCoursesByLikes(userId);
        return ResponseEntity.ok(courses);
    }

    /**
     * 내 코스 조회
     */
    @GetMapping("/my")
    public ResponseEntity<List<CourseListResponse>> getMyCourses(
        @RequestParam("userId") Long userId
    ) {
        List<CourseListResponse> myStores = courseService.getMyCourses(userId);
        return ResponseEntity.ok(myStores);
    }

    /**
     * 코스 좋아요 추가
     */
    @PostMapping("/like")
    public ResponseEntity<Void> likeCourse(@RequestBody CourseLikeRequestDto courseLikeRequestDto) {
        courseService.addCourseLike(courseLikeRequestDto.getCourseId(), courseLikeRequestDto.getUserId());
        return ResponseEntity.ok(null);
    }

    /**
     * 코스 좋아요 취소
     */
    @DeleteMapping("/{courseId}/like")
    public ResponseEntity<Void> unlikeCourse(@PathVariable Long courseId,
        @RequestParam Long userId) {
        courseService.removeCourseLike(courseId, userId);
        return ResponseEntity.ok(null);
    }
}


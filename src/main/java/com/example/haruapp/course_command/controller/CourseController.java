package com.example.haruapp.course_command.controller;

import java.util.ArrayList;
import java.util.List;

import com.example.haruapp.course.dto.response.CourseListResponse;
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
    private static int likeCount = 0;

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

    @GetMapping("/popular")
    public ResponseEntity<List<CourseListResponse>> courseList() {

        List<CourseListResponse> courses = new ArrayList<>();
        courses.add(new CourseListResponse(
            1L,
            "봄날의 산책",
            "행복",
            null,
            "맑음",
            1L,
            "홍길동",
            5,
            List.of("아뜰리에 아티산 (의류 수선)", "금강안경 (안경)", "레이로우", "쿠에른 컨셉스토어", "영화관"),
            likeCount,
            false));
        courses.add(new CourseListResponse(
            2L,
            "여름의 바다",
            "즐거움",
            "신남",
            "맑음",
            2L,
            "김철수",
            3,
            List.of("해변", "수영장", "바베큐장"),
            5,
            true));
        courses.add(new CourseListResponse(
            3L,
            "가을의 단풍",
            "감동",
            "차분함",
            "맑음",
            3L,
            "이영희",
            4,
            List.of("산", "공원", "카페"),
            8,
            false));
        courses.add(new CourseListResponse(
            4L,
            "겨울의 눈꽃",
            "설렘",
            "차가움",
            "눈",
            4L,
            "박준영",
            2,
            List.of("스키장", "온천", "카페"),
            6,
            true));
        courses.add(new CourseListResponse(
            5L,
            "도시 탐방",
            "흥미진진",
            "활기참",
            "맑음",
            5L,
            "최지우",
            6,
            List.of("박물관", "미술관", "카페", "레스토랑", "영화관"),
            12,
            false));

        return ResponseEntity.ok(courses);
    }

    @GetMapping("/{courseId}/like")
    public ResponseEntity<Void> likeCourse(@PathVariable Long courseId) {
        likeCount++;
        log.info("Course with ID {} liked. Total likes: {}", courseId, likeCount);
        return ResponseEntity.ok(null);
    }

    @GetMapping("/{courseId}/unlike")
    public ResponseEntity<Void> unlikeCourse(@PathVariable Long courseId) {
        likeCount--;
        log.info("Course with ID {} unliked. Total likes: {}", courseId, likeCount);
        return ResponseEntity.ok(null);
    }
}


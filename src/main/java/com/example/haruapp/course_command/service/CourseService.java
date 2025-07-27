package com.example.haruapp.course_command.service;

import com.example.haruapp.course_command.domain.CourseEntity;
import com.example.haruapp.course_command.domain.CourseStoreEntity;
import com.example.haruapp.course_command.dto.request.CourseSaveRequest;
import com.example.haruapp.course_command.dto.request.CourseUpdateRequest;
import com.example.haruapp.course_command.mapper.CourseMapper;
import com.example.haruapp.global.error.CustomException;
import com.example.haruapp.global.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CourseService {

    private final CourseMapper courseMapper;

    /**
     * 코스 저장하기
     * */
    @Transactional
    public Long saveCourse(CourseSaveRequest request, Long userId) {
        List<Long> stores = request.getStoreList();

        // 코스별 매장 수 6개로 강제
        if (stores == null || stores.size() != 6) {
            throw new CustomException(ErrorCode.COURSE_STORE_COUNT_INVALID);
        }

        Long courseId = courseMapper.getNextCourseId();
        courseMapper.insertCourse(courseId, request.getTitle(), request.getWeather(),
                userId, request.getEmotionId());

        int sequence = 1;

        for (Long storeId : stores) {
            courseMapper.insertCourseStore(courseId, storeId, sequence++);
        }

        return courseId;
    }


    /**
     * 내 코스 수정
     * - 코스 제목, 매장과 매장 순서 수정 가능
     * */
    @Transactional
    public void updateCourse(Long courseId, Long userId, CourseUpdateRequest request) {
        Long ownerId = courseMapper.findCourseOwner(courseId);
        if (!userId.equals(ownerId)) {
            throw new CustomException(ErrorCode.COURSE_UPDATE_UNAUTHORIZED);
        }

        // 코스 제목 업데이트
        courseMapper.updateCourseTitle(courseId, request.getTitle());

        // 기존 course_store 삭제
        courseMapper.deleteCourseStores(courseId);

        // 새로 등록
        int sequence = 1;
        for (Long storeId : request.getStoreList()) {
            courseMapper.insertCourseStore(courseId, storeId, sequence++);
        }
    }


    /**
     * 코스 스크랩
     * - 다른 사람의 코스를 복사해서, 내 코스로 새로 생성
     * */
    @Transactional
    public Long scrapCourse(Long sourceCourseId, Long userId) {
        // 1. 원본 course 조회
        CourseEntity original = courseMapper.findCourseById(sourceCourseId);
        if (original == null) {
            throw new CustomException(ErrorCode.COURSE_NOT_FOUND);
        }
        if (original.getUserId().equals(userId)) {
            throw new CustomException(ErrorCode.COURSE_SCRAP_SELF);
        }

        // 2. 원본 store 목록 조회
        List<CourseStoreEntity> stores = courseMapper.findCourseStoresByCourseId(sourceCourseId);

        // 3. 새 course_id 발급 및 insert
        Long newCourseId = courseMapper.getNextCourseId();
        courseMapper.insertCourse(
                newCourseId,
                original.getTitle(),
                original.getWeather(),
                userId, // 로그인 유저로 소유자 변경
                original.getEmotionId()
        );

        // 4. store 목록 복사
        for (CourseStoreEntity s : stores) {
            courseMapper.insertCourseStore(newCourseId, s.getStoreId(), s.getSequence());
        }

        return newCourseId;
    }
}

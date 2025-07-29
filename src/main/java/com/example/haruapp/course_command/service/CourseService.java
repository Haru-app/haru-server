package com.example.haruapp.course_command.service;

import java.util.List;

import com.example.haruapp.course_command.dto.request.CourseScoreDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.haruapp.course_command.dto.response.CourseListResponse;
import com.example.haruapp.course_command.domain.CourseEntity;
import com.example.haruapp.course_command.domain.CourseStoreEntity;
import com.example.haruapp.course_command.dto.request.CourseSaveRequest;
import com.example.haruapp.course_command.dto.request.CourseUpdateRequest;
import com.example.haruapp.course_command.mapper.CourseMapper;
import com.example.haruapp.global.error.CustomException;
import com.example.haruapp.global.error.ErrorCode;

import lombok.RequiredArgsConstructor;

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

		// 코스 저장
        Long courseId = courseMapper.getNextCourseId();
        courseMapper.insertCourse(courseId, request.getTitle(), request.getWeather(),
                userId, request.getEmotionId());

        int sequence = 1;

		// 코스 매장들 저장
        for (Long storeId : stores) {
            courseMapper.insertCourseStore(courseId, storeId, sequence++);
        }

		// 코스 점수 저장
		CourseScoreDTO score = request.getScore();
		if (score != null) {
			courseMapper.insertCourseScore(
					courseId,
					score.getAverageScore(),
					score.getMaxPossibleScore(),
					score.getDiversityScore(),
					score.getSimilarityStddev()
			);
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

	/**
	 * 좋아요 수가 많은 순으로 코스 목록 조회
	 *
	 * @param userId 현재 로그인한 사용자 ID
	 */
	public List<CourseListResponse> getCoursesSortedByLikes(Long userId, String emotion, String weather, String storeKeyword) {

		return courseMapper.findAllCoursesOrderByLikes(userId, emotion, weather, storeKeyword);
	}

	/**
	 * 코스 좋아요 추가
	 * @param courseId 좋아요할 코스 ID
	 * @param userId 현재 로그인한 사용자 ID
	 */
	@Transactional
	public void addCourseLike(Long courseId, Long userId) {
		if (userId == null) {
			throw new CustomException(ErrorCode.REQUIRE_LOGIN);
		}
		if (courseMapper.findCourseById(courseId) == null) {
			throw new CustomException(ErrorCode.COURSE_NOT_FOUND);
		}

		courseMapper.insertCourseLike(courseId, userId);
	}

	/**
	 * 코스 좋아요 취소
	 * @param courseId 좋아요를 취소할 코스 ID
	 * @param userId 현재 로그인한 사용자 ID
	 */
	@Transactional
	public void removeCourseLike(Long courseId, Long userId) {
		if (userId == null) {
			throw new CustomException(ErrorCode.REQUIRE_LOGIN);
		}
		if (courseMapper.findCourseById(courseId) == null) {
			throw new CustomException(ErrorCode.COURSE_NOT_FOUND);
		}

		courseMapper.deleteCourseLike(courseId, userId);
	}

	/**
	 * 내 코스 목록 조회
	 * @param userId 현재 로그인한 사용자 ID
	 */
	public List<CourseListResponse> getMyCourses(Long userId, String emotion, String weather, String storeKeyword) {
		if(userId == null) {
			throw new CustomException(ErrorCode.REQUIRE_LOGIN);
		}

		return courseMapper.findMyCourses(userId, emotion, weather, storeKeyword);
	}
}

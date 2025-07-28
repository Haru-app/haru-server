package com.example.haruapp.course_command.mapper;

import com.example.haruapp.course_command.dto.response.CourseListResponse;
import com.example.haruapp.course_command.domain.CourseEntity;
import com.example.haruapp.course_command.domain.CourseStoreEntity;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CourseMapper {

    // 다음 코스 아이디 반환
    @Select("SELECT course_seq.NEXTVAL FROM dual")
    Long getNextCourseId();

    // 코스 생성
    @Insert("""
        INSERT INTO course (course_id, title, weather, user_id, emotion_id)
        VALUES (#{courseId}, #{title}, #{weather}, #{userId}, #{emotionId})
    """)
    void insertCourse(@Param("courseId") Long courseId,
                      @Param("title") String title,
                      @Param("weather") String weather,
                      @Param("userId") Long userId,
                      @Param("emotionId") Long emotionId);

    // 코스의 매장 정보 생성
    @Insert("""
        INSERT INTO course_store (course_store_id, course_id, store_id, sequence)
        VALUES (course_store_seq.NEXTVAL, #{courseId}, #{storeId}, #{sequence})
    """)
    void insertCourseStore(@Param("courseId") Long courseId,
                           @Param("storeId") Long storeId,
                           @Param("sequence") int sequence);

    // 코스 생성자 아이디
    @Select("SELECT user_id FROM course WHERE course_id = #{courseId}")
    Long findCourseOwner(Long courseId);

    // 코스 제목 수정
    @Update("""
        UPDATE course
        SET title = #{title}
        WHERE course_id = #{courseId}
    """)
    void updateCourseTitle(@Param("courseId") Long courseId,
                           @Param("title") String title);

    // 코스의 매장 정보 삭제
    @Delete("DELETE FROM course_store WHERE course_id = #{courseId}")
    void deleteCourseStores(@Param("courseId") Long courseId);

    // 코스 아이디로 코스 찾기
    @Select("""
        SELECT * FROM course
        WHERE course_id = #{courseId}
    """)
    CourseEntity findCourseById(Long courseId);

    // 코스 아이디로 매장 리스트 찾기
    @Select("""
        SELECT store_id, sequence FROM course_store
        WHERE course_id = #{courseId}
        ORDER BY sequence
    """)
    List<CourseStoreEntity> findCourseStoresByCourseId(Long courseId);

    /**
     * 특정 코스에 포함된 매장 이름들을 순서대로 조회한다.
     *
     * @param courseId 코스 ID
     * @return 매장 이름 목록 (순서 보장)
     */
    @Select("""
        SELECT s.STORE_NAME
        FROM COURSE_STORE cs
        INNER JOIN STORE s ON cs.STORE_ID = s.STORE_ID
        WHERE cs.COURSE_ID = #{courseId}
        ORDER BY cs.SEQUENCE
        """)
    List<String> findStoreNamesByCourseId(@Param("courseId") Long courseId);

    /**
     * 코스 목록을 좋아요 수가 많은 순으로 조회한다.
     * 좋아요 수가 같다면, 최근 생성된 코스가 우선순위를 가진다.
     *
     * @param userId 현재 로그인한 사용자 ID (좋아요 여부 확인용)
     * @return 인기순으로 정렬된 코스 목록
     */
    @Select("""
        SELECT 
            c.COURSE_ID as courseId,
            c.TITLE as title,
            e.EMOTION_NAME as emotionName,
            c.WEATHER as weather,
            c.USER_ID as userId,
            m.NICKNAME as userNickname,
            COALESCE(store_count.total_stores, 0) as totalStores,
            COALESCE(like_count.like_count, 0) as likeCount,
            CASE 
                WHEN user_like.USER_ID IS NOT NULL THEN 1 
                ELSE 0 
            END as isLiked
        FROM COURSE c
        INNER JOIN MEMBER m ON c.USER_ID = m.USER_ID
        INNER JOIN EMOTION e ON c.EMOTION_ID = e.EMOTION_ID
        LEFT JOIN (
            SELECT 
                cs.COURSE_ID, 
                COUNT(*) as total_stores
            FROM COURSE_STORE cs
            GROUP BY cs.COURSE_ID
        ) store_count ON c.COURSE_ID = store_count.COURSE_ID
        LEFT JOIN (
            SELECT 
                cl.COURSE_ID, 
                COUNT(*) as like_count
            FROM COURSE_LIKE cl
            GROUP BY cl.COURSE_ID
        ) like_count ON c.COURSE_ID = like_count.COURSE_ID
        LEFT JOIN COURSE_LIKE user_like ON c.COURSE_ID = user_like.COURSE_ID 
            AND user_like.USER_ID = #{userId}
        ORDER BY COALESCE(like_count.like_count, 0) DESC, c.CREATED_AT DESC
        """)
        @Results(id = "AllCoursesOrderByLikes", value = {
            @Result(column="courseId",       property="courseId"),
            @Result(column="title",          property="title"),
            @Result(column="emotionName",   property="emotionName"),
            @Result(column="weather",        property="weather"),
            @Result(column="userId",         property="userId"),
            @Result(column="userNickname",   property="userNickname"),
            @Result(column="totalStores",    property="totalStores"),
            @Result(column="likeCount",      property="likeCount"),
            @Result(column="isLiked",        property="isLiked"),
            @Result(property="storeNames", column="courseId", javaType = List.class,
                many=@Many(select="findStoreNamesByCourseId"))
        })
    List<CourseListResponse> findAllCoursesOrderByLikes(@Param("userId") Long userId);

    @Insert("INSERT INTO COURSE_LIKE (COURSE_LIKE_ID, COURSE_ID, USER_ID, CREATED_AT) " +
        "VALUES (COURSE_LIKE_SEQ.NEXTVAL, #{courseId}, #{userId}, SYSDATE)")
    void insertCourseLike(@Param("courseId") Long courseId, @Param("userId") Long userId);

    @Delete("DELETE FROM COURSE_LIKE WHERE COURSE_ID = #{courseId} AND USER_ID = #{userId}")
    void deleteCourseLike(@Param("courseId") Long courseId, @Param("userId") Long userId);

    /**
     * 내가 생성한 코스 목록을 조회한다.
     * 최근에 생성한 코스가 우선순위를 가진다.
     */
    @Select("""
        SELECT 
            c.COURSE_ID as courseId,
            c.TITLE as title,
            e.EMOTION_NAME as emotionName,
            c.WEATHER as weather,
            c.USER_ID as userId,
            m.NICKNAME as userNickname,
            COALESCE(store_count.total_stores, 0) as totalStores,
            COALESCE(like_count.like_count, 0) as likeCount,
            CASE 
                WHEN user_like.USER_ID IS NOT NULL THEN 1 
                ELSE 0 
            END as isLiked
        FROM COURSE c
        INNER JOIN MEMBER m ON c.USER_ID = m.USER_ID
        INNER JOIN EMOTION e ON c.EMOTION_ID = e.EMOTION_ID
        LEFT JOIN (
            SELECT 
                cs.COURSE_ID, 
                COUNT(*) as total_stores
            FROM COURSE_STORE cs
            GROUP BY cs.COURSE_ID
        ) store_count ON c.COURSE_ID = store_count.COURSE_ID
        LEFT JOIN (
            SELECT 
                cl.COURSE_ID, 
                COUNT(*) as like_count
            FROM COURSE_LIKE cl
            GROUP BY cl.COURSE_ID
        ) like_count ON c.COURSE_ID = like_count.COURSE_ID
        LEFT JOIN COURSE_LIKE user_like ON c.COURSE_ID = user_like.COURSE_ID 
            AND user_like.USER_ID = #{userId}
        WHERE c.USER_ID = #{userId}
        ORDER BY c.CREATED_AT DESC
        """)
    @Results(id = "MyCourses", value = {
        @Result(column="courseId",       property="courseId"),
        @Result(column="title",          property="title"),
        @Result(column="emotionName",   property="emotionName"),
        @Result(column="weather",        property="weather"),
        @Result(column="userId",         property="userId"),
        @Result(column="userNickname",   property="userNickname"),
        @Result(column="totalStores",    property="totalStores"),
        @Result(column="likeCount",      property="likeCount"),
        @Result(column="isLiked",        property="isLiked"),
        @Result(property="storeNames", column="courseId", javaType = List.class,
            many=@Many(select="findStoreNamesByCourseId"))
    })
    List<CourseListResponse> findMyCourses(@Param("userId") Long userId);
}

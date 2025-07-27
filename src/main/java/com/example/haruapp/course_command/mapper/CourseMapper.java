package com.example.haruapp.course_command.mapper;

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



}

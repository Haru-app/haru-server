package com.example.haruapp.subscription.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface FcmTokenMapper {

    @Select("SELECT FCM_TOKEN " +
            "FROM MEMBER " +
            "WHERE USER_ID = #{userId}")
    String findFcmTokenByUserId(@Param("userId") Long userId);

    @Update("UPDATE MEMBER " +
            "SET FCM_TOKEN = #{fcmToken} " +
            "WHERE USER_ID = #{userId}")
    void saveFcmToken(@Param("userId") Long userId, @Param("fcmToken")String token);

}

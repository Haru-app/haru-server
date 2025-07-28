package com.example.haruapp.member.mapper;

import com.example.haruapp.member.domain.Member;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface MemberMapper {

    @Select("SELECT USER_ID AS userId, EMAIL, NICKNAME, CUSTOMER_KEY AS customerKey " +
            "FROM MEMBER " +
            "WHERE USER_ID = #{userId}")
    Member findById(@Param("userId") Long userId);

    @Update("UPDATE MEMBER " +
            "SET CUSTOMER_KEY = #{customerKey} " +
            "WHERE USER_ID = #{userId}")
    void updateCustomerKey(@Param("userId") Long userId, @Param("customerKey") String newCustomerKey);

    @Select("SELECT USER_ID AS userId, EMAIL, NICKNAME, CUSTOMER_KEY AS customerKey " +
            "FROM MEMBER " +
            "WHERE CUSTOMER_KEY = #{customerKey}")
    Member findByCustomerKey(String customerKey);

}

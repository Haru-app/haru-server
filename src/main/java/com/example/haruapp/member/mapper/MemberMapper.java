package com.example.haruapp.member.mapper;

import com.example.haruapp.member.domain.Member;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface MemberMapper {

    Member findById(@Param("userId") Long userId);

    void updateCustomerKey(@Param("userId") Long userId, @Param("customerKey") String newCustomerKey);

    Member findByCustomerKey(String customerKey);

}

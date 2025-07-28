package com.example.haruapp.subscription.mapper;

import com.example.haruapp.subscription.domain.Subscription;
import com.example.haruapp.subscription.dto.response.SubscriptionPaymentTargetResponse;
import org.apache.ibatis.annotations.*;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface SubscriptionMapper {

    @Select("SELECT * " +
            "FROM SUBSCRIPTION " +
            "WHERE USER_ID = #{userId} AND STATUS = 'ACTIVE'")
    Subscription findByUserId(@Param("userId") Long userId);

    @Insert("INSERT INTO SUBSCRIPTION " +
            "(SUBSCRIPTION_ID, USER_ID, BILLING_KEY, STARTED_AT, EXPIRES_AT, NEXT_PAYMENT_AT) " +
            "VALUES (SUBSCRIPTION_SEQ.NEXTVAL, #{userId}, #{billingKey}, #{startedAt}, #{expiresAt}, #{nextPaymentAt})")
    void insertSubscription(@Param("userId") Long userId,
                            @Param("billingKey") String billingKey,
                            @Param("startedAt") LocalDateTime startedAt,
                            @Param("expiresAt") LocalDateTime expiresAt,
                            @Param("nextPaymentAt") LocalDateTime nextPaymentAt);

    @Select("SELECT S.USER_ID AS userId, S.BILLING_KEY AS billingKey, M.CUSTOMER_KEY AS customerKey " +
            "FROM SUBSCRIPTION S " +
            "JOIN MEMBER M ON S.USER_ID = M.USER_ID " +
            "WHERE S.IS_AUTO_RENEW = 'Y' AND S.STATUS = 'ACTIVE' AND S.NEXT_PAYMENT_AT = #{today}")
    List<SubscriptionPaymentTargetResponse> findPaymentTargets(@Param("today") LocalDateTime today);

    // 기존 구독 EXPIRED 처리
    @Update("UPDATE SUBSCRIPTION " +
            "SET STATUS = 'EXPIRED' " +
            "WHERE USER_ID = #{userId} AND S.IS_AUTO_RENEW = 'Y' AND S.STATUS = 'ACTIVE' AND NEXT_PAYMENT_AT = #{today}")
    void expireOldSubscription(@Param("userId") Long userId, @Param("today") LocalDateTime today);

    // 실패 시 상태만 기록
    @Update("UPDATE SUBSCRIPTION " +
            "SET STAUTS = 'FAILED' " +
            "WHERE USER_ID = #{userId} AND S.IS_AUTO_RENEW = 'Y' AND S.STATUS = 'ACTIVE' AND NEXT_PAYMENT_AT = #{today}")
    void markPaymentFailed(@Param("userId") Long userId, @Param("today") LocalDateTime today);

}

package com.example.haruapp.subscription.mapper;

import com.example.haruapp.subscription.domain.Subscription;
import com.example.haruapp.subscription.dto.response.SubscriptionPaymentTargetResponse;
import org.apache.ibatis.annotations.*;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface SubscriptionMapper {

    @Select("SELECT * " +
            "FROM SUBSCRIPTION " +
            "WHERE USER_ID = #{userId} AND STATUS = 'ACTIVE'")
    Subscription findByUserId(@Param("userId") Long userId);

   @Insert("INSERT INTO SUBSCRIPTION " +
            "(SUBSCRIPTION_ID, USER_ID, BILLING_KEY, STATUS, STARTED_AT, EXPIRES_AT, NEXT_PAYMENT_AT) " +
            "VALUES (SUBSCRIPTION_SEQ.NEXTVAL, #{userId}, #{billingKey}, #{status}, #{startedAt}, #{expiresAt}, #{nextPaymentAt})")
    void insertSubscription(@Param("userId") Long userId,
                            @Param("billingKey") String billingKey,
                            @Param("status") String status,
                            @Param("startedAt") LocalDate startedAt,
                            @Param("expiresAt") LocalDate expiresAt,
                            @Param("nextPaymentAt") LocalDate nextPaymentAt);

    @Select("SELECT S.USER_ID AS userId, S.BILLING_KEY AS billingKey, M.CUSTOMER_KEY AS customerKey " +
            "FROM SUBSCRIPTION S " +
            "JOIN MEMBER M ON S.USER_ID = M.USER_ID " +
            "WHERE S.IS_AUTO_RENEW = 'Y' AND S.STATUS = 'ACTIVE' AND S.NEXT_PAYMENT_AT = #{today}")
    List<SubscriptionPaymentTargetResponse> findPaymentTargets(@Param("today") LocalDate today);

    // 실패 시 상태만 기록
    @Update("UPDATE SUBSCRIPTION " +
            "SET STAUTS = 'FAILED' " +
            "WHERE USER_ID = #{userId} AND IS_AUTO_RENEW = 'Y' AND STATUS = 'ACTIVE' AND NEXT_PAYMENT_AT = #{today}")
    void markPaymentFailed(@Param("userId") Long userId, @Param("today") LocalDate today);

    // 가장 최근 구독
    @Select("SELECT * FROM (" +
            "SELECT SUBSCRIPTION_ID " +
            "FROM SUBSCRIPTION " +
            "WHERE USER_ID = #{userId} AND IS_AUTO_RENEW = 'Y' AND STATUS = 'ACTIVE' " +
            "ORDER BY STARTED_AT DESC" +
            ") WHERE ROWNUM = 1")
    Long findLatestSubscriptionIdByUserId(@Param("userId") Long userId);

    @Update("UPDATE SUBSCRIPTION " +
            "SET IS_AUTO_RENEW = 'N', STATUS = 'CANCELLED', CANCELLED_AT = CURRENT_TIMESTAMP " +
            "WHERE USER_ID = #{userId} AND SUBSCRIPTION_ID = #{subscriptionId}")
    void cancelSubscription(@Param("userId") Long userId, @Param("subscriptionId")Long subscriptionId);

}

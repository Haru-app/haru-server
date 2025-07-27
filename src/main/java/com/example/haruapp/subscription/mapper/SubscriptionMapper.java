package com.example.haruapp.subscription.mapper;

import com.example.haruapp.subscription.domain.Subscription;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;

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

}

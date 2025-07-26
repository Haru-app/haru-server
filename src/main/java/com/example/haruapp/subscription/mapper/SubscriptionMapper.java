package com.example.haruapp.subscription.mapper;

import com.example.haruapp.subscription.domain.Subscription;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;

@Mapper
public interface SubscriptionMapper {

    Subscription findByUserId(@Param("userId") Long userId);

    void insertSubscription(@Param("userId") Long userId,
                            @Param("billingKey") String billingKey,
                            @Param("startedAt") LocalDateTime startedAt,
                            @Param("expiresAt") LocalDateTime expiresAt,
                            @Param("nextPaymentAt") LocalDateTime nextPaymentAt);

}

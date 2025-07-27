package com.example.haruapp.subscription.domain;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class Subscription {

    private Long subscriptionId;
    private String billingKey;
    private String isAutoRenew;
    private String status;
    private LocalDateTime startedAt;
    private LocalDateTime expiresAt;
    private LocalDateTime cancelledAt;
    private LocalDateTime nextPaymentAt;
    private Long userId;

}

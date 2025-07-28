package com.example.haruapp.subscription.domain;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class Subscription {

    private Long subscriptionId;
    private String billingKey;
    private String isAutoRenew;
    private String status;
    private LocalDate startedAt;
    private LocalDate expiresAt;
    private LocalDate cancelledAt;
    private LocalDate nextPaymentAt;
    private Long userId;

}

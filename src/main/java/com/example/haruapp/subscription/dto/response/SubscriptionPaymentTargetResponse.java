package com.example.haruapp.subscription.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SubscriptionPaymentTargetResponse {

    private Long userId;
    private String billingKey;
    private String customerKey;

}

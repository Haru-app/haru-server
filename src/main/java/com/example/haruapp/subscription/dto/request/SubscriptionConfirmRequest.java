package com.example.haruapp.subscription.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SubscriptionConfirmRequest {

    private String authKey;
    private String customerKey;

}

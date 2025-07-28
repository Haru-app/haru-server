package com.example.haruapp.subscription.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class BillingResponse {

    private String mId;
    private String customerKey;
    private String billingKey;
    private String method;
    private CardInfo card;

    @Getter
    @Setter
    public static class CardInfo {
        private String number;
        private String issuerCode;
        private String acquireCode;
        private String cardType;
        private String ownerType;
    }

}

package com.example.haruapp.subscription.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PaymentResponse {

    private String paymentKey;
    private String orderId;
    private String orderName;
    private String status;
    private String approvedAt;
    private String method;
    private Long totalAmount;
    private Receipt receipt;

    @Getter
    @Setter
    public static class Receipt {
        private String url;
    }

}

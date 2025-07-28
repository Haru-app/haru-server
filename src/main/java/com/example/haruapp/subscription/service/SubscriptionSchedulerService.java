package com.example.haruapp.subscription.service;

import com.example.haruapp.subscription.dto.response.SubscriptionPaymentTargetResponse;
import com.example.haruapp.subscription.external.TossPaymentsClient;
import com.example.haruapp.subscription.mapper.SubscriptionMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SubscriptionSchedulerService {

    private final SubscriptionMapper subscriptionMapper;
    private final TossPaymentsClient tossPaymentsClient;    // billingKey 결제 요청용

    public void executeScheduledPayments() {
        LocalDate now = LocalDate.now();
        List<SubscriptionPaymentTargetResponse> dueList = subscriptionMapper.findPaymentTargets(now);

        for (SubscriptionPaymentTargetResponse target : dueList) {
            try {
                tossPaymentsClient.requestAutoPayment(target.getBillingKey(), target.getCustomerKey());
                subscriptionMapper.insertSubscription(
                        target.getUserId(),
                        target.getBillingKey(),
                        now,
                        now.plusMonths(1),
                        now.plusMonths(1)
                );
                log.info("자동결제 성공: {}", target.getUserId());
            } catch (Exception e) {
                log.warn("자동결제 실패: {} - {}", target.getUserId(), e.getMessage());
                subscriptionMapper.markPaymentFailed(target.getUserId(), now);
                // TODO 알림 보내기
            }
        }
    }

}

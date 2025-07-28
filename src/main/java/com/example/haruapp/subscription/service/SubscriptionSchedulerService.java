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
    private final FcmService fcmService;

    public void executeScheduledPayments() {
        LocalDate now = LocalDate.now();
        List<SubscriptionPaymentTargetResponse> dueList = subscriptionMapper.findPaymentTargets(now);

        for (SubscriptionPaymentTargetResponse target : dueList) {
            try {
                tossPaymentsClient.requestAutoPayment(target.getBillingKey(), target.getCustomerKey());
                subscriptionMapper.insertSubscription(
                        target.getUserId(),
                        target.getBillingKey(),
                        "ACTIVE",
                        now,
                        now.plusMonths(1),
                        now.plusMonths(1)
                );
                fcmService.sendNotification(
                        target.getUserId(),
                        "HaRU 감정 카드 정기 구독 결제 완료 🎉",
                        "감정 카드를 생성해 보세요! \uD83D\uDCF8"
                );
                log.info("자동결제 성공: {}", target.getUserId());
            } catch (Exception e) {
                log.warn("자동결제 실패: {} - {}", target.getUserId(), e.getMessage());
                subscriptionMapper.insertSubscription(
                        target.getUserId(),
                        target.getBillingKey(),
                        "FAILED",
                        now,
                        now.plusMonths(1),
                        now.plusMonths(1)
                );
                fcmService.sendNotification(
                        target.getUserId(),
                        "HaRU 감정 카드 정기 구독 결제 실패 \uD83D\uDE25",
                        "결제 수단을 다시 확인해 주세요!"
                );
            }
        }
    }

}

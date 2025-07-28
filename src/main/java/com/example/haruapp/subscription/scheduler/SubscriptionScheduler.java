package com.example.haruapp.subscription.scheduler;

import com.example.haruapp.subscription.service.SubscriptionSchedulerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class SubscriptionScheduler {

    private final SubscriptionSchedulerService subscriptionSchedulerService;

    @Scheduled(cron = "0 0 4 * * ?")    // 매일 새벽 4시
    public void runDailyAutoPayment() {
        log.info("[Scheduler] 자동 결제 실행 시작");
        subscriptionSchedulerService.executeScheduledPayments();
    }

}

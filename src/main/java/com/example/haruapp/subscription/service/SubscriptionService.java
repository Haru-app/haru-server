package com.example.haruapp.subscription.service;

import com.example.haruapp.global.error.CustomException;
import com.example.haruapp.global.error.ErrorCode;
import com.example.haruapp.global.model.MailType;
import com.example.haruapp.global.service.MailService;
import com.example.haruapp.member.domain.Member;
import com.example.haruapp.member.mapper.MemberMapper;
import com.example.haruapp.subscription.domain.Subscription;
import com.example.haruapp.subscription.dto.response.BillingResponse;
import com.example.haruapp.subscription.dto.response.PaymentResponse;
import com.example.haruapp.subscription.external.TossPaymentsClient;
import com.example.haruapp.subscription.mapper.SubscriptionMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class SubscriptionService {

    private final MemberMapper memberMapper;
    private final SubscriptionMapper subscriptionMapper;
    private final TossPaymentsClient tossPaymentsClient;
    private final FcmService fcmService;
    private final MailService mailService;

    public String getOrCreateCustomerKey(Long userId) {
        Member member = memberMapper.findById(userId);
        if (member == null) {
            throw new CustomException(ErrorCode.USER_NOT_FOUND);
        }

        Subscription subscription = subscriptionMapper.findByUserId(userId);
        if (subscription != null && subscription.getBillingKey() != null) {
            throw new CustomException(ErrorCode.ALREADY_SUBSCRIBED);
        }

        // customerKey가 있는 경우
        if (member.getCustomerKey() != null) {
            return member.getCustomerKey();
        }

        // customerKey가 없는 경우 → 생성 및 저장
        String newCustomerKey = "user-" + UUID.randomUUID().toString().replaceAll("[^A-Za-z0-9\\-_.@=]", "");
        memberMapper.updateCustomerKey(userId, newCustomerKey);
        return newCustomerKey;
    }

    public void confirmBillingKey(String authKey, String customerKey) {
        //  Toss에 billingKey 요청
        BillingResponse billing = tossPaymentsClient.requestBillingKey(authKey, customerKey);

        Member member = memberMapper.findByCustomerKey(customerKey);
        if (member == null) {
            throw new CustomException(ErrorCode.USER_NOT_FOUND);
        }

        // 기존 구독 존재 여부 확인
        Subscription sub = subscriptionMapper.findByUserId(member.getUserId());
        if (sub != null && sub.getBillingKey() != null) {
            throw new CustomException(ErrorCode.ALREADY_SUBSCRIBED);
        }

        // billingKey 기반 자동결제 요청
        PaymentResponse payment = tossPaymentsClient.requestAutoPayment(billing.getBillingKey(), customerKey);
        if (!"DONE".equals(payment.getStatus())) {
            throw new CustomException(ErrorCode.SERVER_ERROR);
        }

        // 구독 저장
        LocalDate now = LocalDate.now();
        subscriptionMapper.insertSubscription(
                member.getUserId(),
                billing.getBillingKey(),
                "ACTIVE",
                now,
                now.plusMonths(1),
                now.plusMonths(1)
        );

        fcmService.sendNotification(
                member.getUserId(),
                "HaRU 감정 카드 정기 구독 결제 완료 🎉",
                "감정 카드를 생성해 보세요! \uD83D\uDCF8"
        );
        sendSubscriptionSuccessEmail(member, now, now.plusMonths(1));
    }

    private void sendSubscriptionSuccessEmail(Member member, LocalDate startedAt, LocalDate expiresAt) {
        try {
            Map<String, Object> vars = new HashMap<>();
            vars.put("username", member.getNickname());
            vars.put("amount", "2,900");
            vars.put("startedAt", startedAt.toString());
            vars.put("expiresAt", expiresAt.toString());

            mailService.sendMail(member.getEmail(), MailType.SUBSCRIPTION_COMPLETE, vars);
        } catch (Exception e) {
            log.warn("구독 완료 이메일 전송 실패: {}", e.getMessage());
        }
    }

    @Transactional
    public void cancelSubscription(Long userId) {
        Long subscriptionId = subscriptionMapper.findLatestSubscriptionIdByUserId(userId);
        subscriptionMapper.cancelSubscription(userId, subscriptionId);
    }

}

package com.example.haruapp.subscription.controller;

import com.example.haruapp.subscription.dto.request.SubscriptionConfirmRequest;
import com.example.haruapp.subscription.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/subscriptions")
@RequiredArgsConstructor
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    @GetMapping("/customer-key")
    public ResponseEntity<String> getCustomerKey(@RequestParam Long userId) {
        String customerKey = subscriptionService.getOrCreateCustomerKey(userId);
        return ResponseEntity.ok(customerKey);
    }

    @PostMapping("/confirm")
    public ResponseEntity<Void> confirmBillingKey(@RequestBody SubscriptionConfirmRequest request) {
        subscriptionService.confirmBillingKey(request.getAuthKey(), request.getCustomerKey());
        return ResponseEntity.ok().build();
    }

}

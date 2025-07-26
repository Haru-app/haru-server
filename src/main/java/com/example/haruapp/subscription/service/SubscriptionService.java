package com.example.haruapp.subscription.service;

public interface SubscriptionService {

    String getOrCreateCustomerKey(Long userId);

    void confirmBillingKey(String authKey, String customerKey);

}

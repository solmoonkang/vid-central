package com.vidcentral.api.domain.subscription.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vidcentral.api.domain.subscription.entity.Subscription;

public interface SubscriptionRepository extends JpaRepository<Long, Subscription> {
}

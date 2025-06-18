package com.vidcentral.api.domain.subscription.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vidcentral.api.domain.member.entity.Member;
import com.vidcentral.api.domain.subscription.entity.Subscription;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {

	Optional<Subscription> findBySubscriberAndChannelOwner(Member subscriber, Member channelOwner);

	long countByChannelOwner(Member channelOwner);
}

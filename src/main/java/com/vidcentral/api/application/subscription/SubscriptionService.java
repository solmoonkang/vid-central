package com.vidcentral.api.application.subscription;

import static com.vidcentral.global.error.model.ErrorMessage.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vidcentral.api.application.member.MemberReadService;
import com.vidcentral.api.domain.auth.entity.AuthMember;
import com.vidcentral.api.domain.member.entity.Member;
import com.vidcentral.api.domain.subscription.entity.Subscription;
import com.vidcentral.api.domain.subscription.repository.SubscriptionRepository;
import com.vidcentral.api.dto.subscription.response.SubscriptionResponse;
import com.vidcentral.global.error.exception.BadRequestException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SubscriptionService {

	private final MemberReadService memberReadService;

	private final SubscriptionRepository subscriptionRepository;

	public SubscriptionResponse toggleSubscription(AuthMember authMember, Long channelOwnerId) {
		final Member subscriber = memberReadService.findMember(authMember.email());
		final Member channelOwner = memberReadService.readMember(channelOwnerId);

		validateNotSelfSubscription(subscriber.getId(), channelOwner.getId());

		final boolean subscribed = subscriptionRepository.findBySubscriberAndChannelOwner(subscriber, channelOwner)
			.map(this::unsubscribe)
			.orElseGet(() -> subscribe(subscriber, channelOwner));

		return new SubscriptionResponse(subscribed);
	}

	private boolean subscribe(Member subscriber, Member channelOwner) {
		final Subscription subscription = Subscription.createSubscription(subscriber, channelOwner);
		subscriptionRepository.save(subscription);
		return true;
	}

	private boolean unsubscribe(Subscription existingSubscription) {
		subscriptionRepository.delete(existingSubscription);
		return false;
	}

	private void validateNotSelfSubscription(Long subscriberId, Long channelOwnerId) {
		if (subscriberId.equals(channelOwnerId)) {
			throw new BadRequestException(FAILED_NOT_SELF_SUBSCRIPTION_ERROR);
		}
	}
}

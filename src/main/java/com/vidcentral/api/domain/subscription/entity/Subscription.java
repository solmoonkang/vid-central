package com.vidcentral.api.domain.subscription.entity;

import com.vidcentral.api.domain.member.entity.Member;
import com.vidcentral.global.common.entity.BaseTimeEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(
	name = "SUBSCRIPTIONS",
	uniqueConstraints = @UniqueConstraint(
		columnNames = {"subscriber_id", "channel_owner_id"}
	)
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Subscription extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "subscription_id")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "subscriber_id", nullable = false)
	private Member subscriber;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "channel_owner_id", nullable = false)
	private Member channelOwner;

	private Subscription(Member subscriber, Member channelOwner) {
		this.subscriber = subscriber;
		this.channelOwner = channelOwner;
	}

	public static Subscription createSubscription(Member subscriber, Member channelOwner) {
		return new Subscription(subscriber, channelOwner);
	}
}

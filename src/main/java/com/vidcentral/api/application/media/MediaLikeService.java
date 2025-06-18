package com.vidcentral.api.application.media;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vidcentral.api.application.member.MemberReadService;
import com.vidcentral.api.domain.auth.entity.AuthMember;
import com.vidcentral.api.domain.media.entity.Media;
import com.vidcentral.api.domain.media.entity.MediaLike;
import com.vidcentral.api.domain.media.repository.MediaLikeRepository;
import com.vidcentral.api.domain.member.entity.Member;
import com.vidcentral.api.dto.media.request.ReactionRequest;
import com.vidcentral.api.dto.media.response.ReactionCountResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MediaLikeService {

	private final MemberReadService memberReadService;
	private final MediaReadService mediaReadService;
	private final MediaLikeRepository mediaLikeRepository;

	@Transactional
	public void toggleReaction(AuthMember authMember, Long mediaId, ReactionRequest reactionRequest) {
		final Member member = memberReadService.findMember(authMember.email());
		final Media media = mediaReadService.readMedia(mediaId);

		mediaLikeRepository.findByMediaAndMember(media, member)
			.ifPresentOrElse(
				existingMediaLike -> handleExistingReaction(existingMediaLike, reactionRequest.isLike()),
				() -> createNewReaction(media, member, reactionRequest.isLike())
			);
	}

	public ReactionCountResponse getReactionAllCounts(Long mediaId) {
		final Media media = mediaReadService.readMedia(mediaId);

		final long likeCount = mediaLikeRepository.countByMediaAndIsLike(media, true);
		final long dislikeCount = mediaLikeRepository.countByMediaAndIsLike(media, false);

		return new ReactionCountResponse(likeCount, dislikeCount);
	}

	private void createNewReaction(Media media, Member member, boolean isLike) {
		final MediaLike mediaLike = MediaLike.createLike(media, member, isLike);
		mediaLikeRepository.save(mediaLike);
	}

	private void handleExistingReaction(MediaLike existingMediaLike, boolean isLike) {
		if (existingMediaLike.isLike() == isLike) {
			mediaLikeRepository.delete(existingMediaLike);
			return;
		}

		existingMediaLike.updateIsLike(isLike);
	}
}

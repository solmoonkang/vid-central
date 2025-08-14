package com.vidcentral.api.application.reaction;

import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vidcentral.api.application.member.MemberReadService;
import com.vidcentral.api.application.video.VideoReadService;
import com.vidcentral.api.domain.member.entity.Member;
import com.vidcentral.api.domain.reaction.entity.VideoReaction;
import com.vidcentral.api.domain.reaction.model.ReactionType;
import com.vidcentral.api.domain.video.entity.Video;
import com.vidcentral.api.dto.reaction.request.ToggleReactionRequest;
import com.vidcentral.global.auth.model.AuthMember;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class VideoReactionService {

	private final VideoReadService videoReadService;
	private final MemberReadService memberReadService;
	private final VideoReactionReadService videoReactionReadService;
	private final VideoReactionWriteService videoReactionWriteService;

	@Transactional
	public void toggleVideoReaction(Long videoId, AuthMember authMember, ToggleReactionRequest toggleReactionRequest) {
		final Video video = videoReadService.findVideoById(videoId);
		final ReactionType newReactionType = toggleReactionRequest.reactionType();

		videoReactionReadService.findExistingVideoReaction(authMember.id(), videoId)
			.ifPresentOrElse(
				existingReaction -> processExistingReaction(video, existingReaction, newReactionType),
				() -> processNewReaction(authMember.id(), video, newReactionType)
			);
	}

	private void processExistingReaction(Video video, VideoReaction existingReaction, ReactionType newReactionType) {
		Map<Boolean, Runnable> actions = Map.of(
			true, () -> videoReactionWriteService.deleteVideoReaction(video, existingReaction),
			false, () -> videoReactionWriteService.changeVideoReaction(video, existingReaction, newReactionType)
		);

		actions.get(existingReaction.getReactionType().equals(newReactionType)).run();
	}

	private void processNewReaction(Long memberId, Video video, ReactionType newReactionType) {
		final Member member = memberReadService.findMemberById(memberId);
		videoReactionWriteService.saveVideoReaction(member, video, newReactionType);
	}
}

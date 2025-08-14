package com.vidcentral.api.application.reaction;

import static com.vidcentral.api.domain.reaction.model.ReactionType.*;

import java.util.Collections;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.vidcentral.api.domain.member.entity.Member;
import com.vidcentral.api.domain.reaction.entity.VideoReaction;
import com.vidcentral.api.domain.reaction.model.ReactionType;
import com.vidcentral.api.domain.reaction.repository.VideoReactionRepository;
import com.vidcentral.api.domain.video.entity.Video;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class VideoReactionWriteService {

	private final VideoReactionRepository videoReactionRepository;

	public void saveVideoReaction(Member member, Video video, ReactionType reactionType) {
		final VideoReaction videoReaction = VideoReaction.createVideoReaction(member, video, reactionType);
		videoReactionRepository.save(videoReaction);
		updateCounter(video, reactionType, 1);
	}

	public void changeVideoReaction(Video video, VideoReaction videoReaction, ReactionType reactionType) {
		updateCounter(video, videoReaction.getReactionType(), -1);
		videoReaction.updateReactionType(reactionType);
		updateCounter(video, reactionType, 1);
	}

	public void deleteVideoReaction(Video video, VideoReaction videoReaction) {
		videoReactionRepository.delete(videoReaction);
		updateCounter(video, videoReaction.getReactionType(), -1);
	}

	private void updateCounter(Video video, ReactionType reactionType, int delta) {
		Map<ReactionType, Map<Integer, Runnable>> counterUpdates = Map.of(
			LIKE, Map.of(1, video::incrementLikeCount, -1, video::decrementLikeCount),
			DISLIKE, Map.of(1, video::incrementDislikeCount, -1, video::decrementDislikeCount)
		);

		counterUpdates.getOrDefault(reactionType, Collections.emptyMap())
			.getOrDefault(delta, () -> {})
			.run();
	}
}

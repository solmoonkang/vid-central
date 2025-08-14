package com.vidcentral.api.application.reaction;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.vidcentral.api.domain.reaction.entity.VideoReaction;
import com.vidcentral.api.domain.reaction.repository.VideoReactionRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class VideoReactionReadService {

	private final VideoReactionRepository videoReactionRepository;

	public Optional<VideoReaction> findExistingVideoReaction(Long memberId, Long videoId) {
		return videoReactionRepository.findByMemberIdAndVideoId(memberId, videoId);
	}
}

package com.vidcentral.api.application.comment;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vidcentral.api.application.member.MemberReadService;
import com.vidcentral.api.application.video.VideoReadService;
import com.vidcentral.api.domain.member.entity.Member;
import com.vidcentral.api.domain.video.entity.Video;
import com.vidcentral.api.dto.comment.request.CreateCommentRequest;
import com.vidcentral.global.auth.model.AuthMember;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommentService {

	private final MemberReadService memberReadService;
	private final VideoReadService videoReadService;
	private final CommentWriteService commentWriteService;

	@Transactional
	public void saveComment(Long videoId, AuthMember authMember, CreateCommentRequest createCommentRequest) {
		final Member member = memberReadService.getMemberById(authMember.id());
		final Video video = videoReadService.getVideoById(videoId);
		commentWriteService.saveComment(member, video, createCommentRequest);
	}
}

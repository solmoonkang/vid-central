package com.vidcentral.api.application.comment;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vidcentral.api.application.member.MemberReadService;
import com.vidcentral.api.application.video.VideoReadService;
import com.vidcentral.api.domain.auth.entity.AuthMember;
import com.vidcentral.api.domain.comment.entity.Comment;
import com.vidcentral.api.domain.comment.repository.CommentRepository;
import com.vidcentral.api.domain.member.entity.Member;
import com.vidcentral.api.domain.video.entity.Video;
import com.vidcentral.api.dto.request.comment.UploadCommentRequest;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService {

	private final MemberReadService memberReadService;
	private final VideoReadService videoReadService;
	private final CommentRepository commentRepository;

	@Transactional
	public void uploadComment(AuthMember authMember, Long videoId, UploadCommentRequest uploadCommentRequest) {
		final Member member = memberReadService.findMember(authMember.email());
		final Video video = videoReadService.findVideo(videoId);

		final Comment comment = CommentMapper.toComment(member, video, uploadCommentRequest.content());
		commentRepository.save(comment);
	}
}

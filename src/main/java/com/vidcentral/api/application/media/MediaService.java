package com.vidcentral.api.application.media;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vidcentral.api.application.media.strategy.AttachmentStrategy;
import com.vidcentral.api.application.member.MemberReadService;
import com.vidcentral.api.domain.auth.entity.AuthMember;
import com.vidcentral.api.domain.media.entity.Media;
import com.vidcentral.api.domain.media.repository.MediaRepository;
import com.vidcentral.api.domain.member.entity.Member;
import com.vidcentral.api.dto.media.request.UploadMediaRequest;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MediaService {

	private final MemberReadService memberReadService;
	private final MediaRepository mediaRepository;
	private final List<AttachmentStrategy> attachmentStrategies;

	@Transactional
	public void uploadMedia(AuthMember authMember, UploadMediaRequest uploadMediaRequest) {
		final Member member = memberReadService.findMember(authMember.email());
		final Media media = mediaRepository.save(Media.createMedia(member, uploadMediaRequest));

		final AttachmentStrategy attachmentStrategy = attachmentStrategies.stream()
			.filter(strategy -> strategy.supportType() == uploadMediaRequest.mediaType())
			.findFirst()
			.orElseThrow(() -> new IllegalStateException("[❎ ERROR] 지원하지 않는 미디어 타입입니다."));

		attachmentStrategy.handle(media, uploadMediaRequest.mediaFiles(), uploadMediaRequest.thumbnailFile());
	}
}

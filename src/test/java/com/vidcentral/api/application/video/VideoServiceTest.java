package com.vidcentral.api.application.video;

import static com.vidcentral.global.error.model.ErrorCode.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.vidcentral.api.application.member.MemberReadService;
import com.vidcentral.api.domain.member.entity.Member;
import com.vidcentral.api.domain.video.entity.Video;
import com.vidcentral.api.dto.video.request.CreateVideoRequest;
import com.vidcentral.fixtures.auth.AuthFixture;
import com.vidcentral.fixtures.member.MemberFixture;
import com.vidcentral.fixtures.video.VideoFixture;
import com.vidcentral.global.auth.model.AuthMember;
import com.vidcentral.global.error.exception.NotFoundException;

@ExtendWith(MockitoExtension.class)
@DisplayName("VideoService 단위 테스트")
class VideoServiceTest {

	@Mock
	private MemberReadService memberReadService;

	@Mock
	private VideoWriteService videoWriteService;

	@InjectMocks
	private VideoService videoService;

	@Nested
	@DisplayName("비디오 메타데이터 저장 테스트: saveVideo")
	class SaveVideoTest {

		@Nested
		@DisplayName("비디오 메타데이터 저장 성공 테스트")
		class SaveVideoSuccess {
			@Test
			@DisplayName("[✅ SUCCESS] saveVideo: 비디오 메타데이터를 성공적으로 저장했습니다.")
			void saveVideo_success() {
				// GIVEN
				final AuthMember authMember = AuthFixture.createAuthMember().build();
				final CreateVideoRequest createVideoRequest = VideoFixture.createCreateVideoRequest().build();
				final Member member = MemberFixture.createMemberEntity();
				final Video video = VideoFixture.createVideoMetadata(member);

				when(memberReadService.findMemberById(anyLong())).thenReturn(member);
				when(videoWriteService.saveVideoMetadata(any(Member.class), any(CreateVideoRequest.class)))
					.thenReturn(video);

				// WHEN
				final Long videoId = videoService.saveVideo(authMember, createVideoRequest);

				// THEN
				verify(memberReadService, times(1)).findMemberById(authMember.id());
				verify(videoWriteService, times(1)).saveVideoMetadata(member, createVideoRequest);
				assertThat(videoId).isEqualTo(video.getId());
			}
		}

		@Nested
		@DisplayName("비디오 메타데이터 저장 실패 테스트")
		class SaveVideoFailure {
			@Test
			@DisplayName("[❎ FAILURE] saveVideo: 존재하지 않는 사용자의 요청으로 비디오 메타데이터 저장에 실패했습니다.")
			void saveVideo_notFoundException_fail() {
				// GIVEN
				final AuthMember notExisteAauthMember = AuthFixture.createAuthMember().build();
				final CreateVideoRequest createVideoRequest = VideoFixture.createCreateVideoRequest().build();

				when(memberReadService.findMemberById(anyLong()))
					.thenThrow(new NotFoundException(MEMBER_NOT_FOUND));

				// WHEN & THEN
				assertThatThrownBy(() -> videoService.saveVideo(notExisteAauthMember, createVideoRequest))
					.isInstanceOf(NotFoundException.class)
					.hasMessageContaining(MEMBER_NOT_FOUND.getMessage());

				verify(videoWriteService, never()).saveVideoMetadata(any(Member.class), any(CreateVideoRequest.class));
			}
		}
	}
}

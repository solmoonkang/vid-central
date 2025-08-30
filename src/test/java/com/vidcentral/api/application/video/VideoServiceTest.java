package com.vidcentral.api.application.video;

import static com.vidcentral.fixtures.FixtureConstant.*;
import static com.vidcentral.global.error.model.ErrorCode.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.vidcentral.api.application.member.MemberReadService;
import com.vidcentral.api.application.recommend.RecommendationService;
import com.vidcentral.api.domain.member.entity.Member;
import com.vidcentral.api.domain.video.entity.Video;
import com.vidcentral.api.domain.video.repository.VideoUploadRedisRepository;
import com.vidcentral.api.dto.pagination.PagedResponse;
import com.vidcentral.api.dto.video.request.CreateVideoRequest;
import com.vidcentral.api.dto.video.request.UploadChunkRequest;
import com.vidcentral.api.dto.video.response.VideoResponse;
import com.vidcentral.fixtures.auth.AuthFixture;
import com.vidcentral.fixtures.member.MemberFixture;
import com.vidcentral.fixtures.video.VideoFixture;
import com.vidcentral.global.auth.model.AuthMember;
import com.vidcentral.global.error.exception.BadRequestException;
import com.vidcentral.global.error.exception.NotFoundException;
import com.vidcentral.global.error.exception.UnauthorizedException;

@ExtendWith(MockitoExtension.class)
@DisplayName("VideoService 단위 테스트")
class VideoServiceTest {

	@Mock
	private MemberReadService memberReadService;

	@Mock
	private VideoWriteService videoWriteService;

	@Mock
	private VideoReadService videoReadService;

	@Mock
	private RecommendationService recommendationService;

	@Mock
	private VideoUploadRedisRepository videoUploadRedisRepository;

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

	@Nested
	@DisplayName("비디오 청크 업로드 테스트: uploadVideoChunk")
	class UploadVideoChunkTest {

		@Nested
		@DisplayName("비디오 청크 업로드 성공 테스트")
		class UploadVideoChunkSuccess {
			@Test
			@DisplayName("[✅ SUCCESS] uploadVideoChunk: 비디오 청크 업로드를 성공적으로 완료했습니다.")
			void uploadVideoChunk_success() {
				// GIVEN
				final AuthMember authMember = AuthFixture.createAuthMember().build();
				final Member member = mock(Member.class);
				final Video video = mock(Video.class);
				final UploadChunkRequest uploadChunkRequest = VideoFixture.createUploadChunkRequest().build();

				when(member.getId()).thenReturn(authMember.id());
				when(video.getMember()).thenReturn(member);

				when(videoReadService.findVideoById(anyLong())).thenReturn(video);
				when(videoUploadRedisRepository.getTotalChunks(anyLong()))
					.thenReturn(Optional.of(uploadChunkRequest.totalChunks()));
				when(videoUploadRedisRepository.hasChunk(anyLong(), anyInt())).thenReturn(false);
				doNothing().when(videoWriteService).uploadVideoChunk(any(Video.class), any(UploadChunkRequest.class));

				// WHEN
				videoService.uploadVideoChunk(video.getId(), authMember, uploadChunkRequest);

				// THEN
				verify(videoReadService, times(1)).findVideoById(video.getId());
				verify(videoUploadRedisRepository, times(1)).getTotalChunks(video.getId());
				verify(videoUploadRedisRepository, times(1)).hasChunk(video.getId(), uploadChunkRequest.chunkNumber());
				verify(videoWriteService, times(1)).uploadVideoChunk(video, uploadChunkRequest);
			}
		}

		@Nested
		@DisplayName("비디오 청크 업로드 실패 테스트")
		class UploadVideoChunkFailure {
			@Test
			@DisplayName("[❎ FAILURE] uploadVideoChunk: 존재하지 않는 사용자의 요청으로 비디오 청크 업로드에 실패했습니다.")
			void uploadVideoChunk_notFoundException_fail() {
				// GIVEN
				final Long notExistedVideoId = 999L;
				final AuthMember authMember = AuthFixture.createAuthMember().build();
				final UploadChunkRequest uploadChunkRequest = VideoFixture.createUploadChunkRequest().build();

				when(videoReadService.findVideoById(anyLong()))
					.thenThrow(new NotFoundException(VIDEO_NOT_FOUND));

				// WHEN & THEN
				assertThatThrownBy(
					() -> videoService.uploadVideoChunk(notExistedVideoId, authMember, uploadChunkRequest))
					.isInstanceOf(NotFoundException.class)
					.hasMessageContaining(VIDEO_NOT_FOUND.getMessage());

				verify(videoWriteService, never()).uploadVideoChunk(any(Video.class), any(UploadChunkRequest.class));
			}

			@Test
			@DisplayName("[❎ FAILURE] uploadVideoChunk: 비디오 소유자가 불일치하여 비디오 청크 업로드에 실패했습니다.")
			void uploadVideoChunk_unauthorizedException_forbiddenVideoModify_fail() {
				// GIVEN
				final AuthMember forbiddenAuthMember = AuthFixture.createAuthMember().id(DEFAULT_ID + 1).build();
				final Member member = mock(Member.class);
				final Video video = mock(Video.class);
				final UploadChunkRequest uploadChunkRequest = VideoFixture.createUploadChunkRequest().build();

				when(member.getId()).thenReturn(DEFAULT_ID);
				when(video.getMember()).thenReturn(member);

				when(videoReadService.findVideoById(anyLong())).thenReturn(video);

				// WHEN & THEN
				assertThatThrownBy(
					() -> videoService.uploadVideoChunk(video.getId(), forbiddenAuthMember, uploadChunkRequest))
					.isInstanceOf(UnauthorizedException.class)
					.hasMessageContaining(FORBIDDEN_VIDEO_MODIFY.getMessage());

				verify(videoUploadRedisRepository, never()).getTotalChunks(anyLong());
				verify(videoWriteService, never()).uploadVideoChunk(any(Video.class), any(UploadChunkRequest.class));
			}

			@Test
			@DisplayName("[❎ FAILURE] uploadVideoChunk: 청크 번호가 유효하지 않아 비디오 청크 업로드에 실패했습니다.")
			void uploadVideoChunk_badRequestException_invalidChunkNumberRange_fail() {
				// GIVEN
				final AuthMember authMember = AuthFixture.createAuthMember().build();
				final Member member = mock(Member.class);
				final Video video = mock(Video.class);
				final UploadChunkRequest invalidChunkRequest = VideoFixture.createUploadChunkRequest()
					.chunkNumber(DEFAULT_CHUNK_NUMBER + 10)
					.build();

				when(member.getId()).thenReturn(authMember.id());
				when(video.getMember()).thenReturn(member);

				when(videoReadService.findVideoById(anyLong())).thenReturn(video);
				when(videoUploadRedisRepository.getTotalChunks(anyLong()))
					.thenReturn(Optional.of(DEFAULT_TOTAL_CHUNKS));

				// WHEN & THEN
				assertThatThrownBy(() -> videoService.uploadVideoChunk(video.getId(), authMember, invalidChunkRequest))
					.isInstanceOf(BadRequestException.class)
					.hasMessageContaining(INVALID_CHUNK_NUMBER_RANGE.getMessage());

				verify(videoUploadRedisRepository, never()).hasChunk(anyLong(), anyInt());
				verify(videoWriteService, never()).uploadVideoChunk(any(Video.class), any(UploadChunkRequest.class));
			}

			@Test
			@DisplayName("[❎ FAILURE] uploadVideoChunk: 총 청크 수가 불일치하여 비디오 청크 업로드에 실패했습니다.")
			void uploadVideoChunk_badRequestException_totalChunksMismatch_fail() {
				// GIVEN
				final AuthMember authMember = AuthFixture.createAuthMember().build();
				final Member member = mock(Member.class);
				final Video video = mock(Video.class);
				final UploadChunkRequest mismatchChunkRequest = VideoFixture.createUploadChunkRequest()
					.totalChunks(5)
					.build();

				when(member.getId()).thenReturn(authMember.id());
				when(video.getMember()).thenReturn(member);

				when(videoReadService.findVideoById(anyLong())).thenReturn(video);
				when(videoUploadRedisRepository.getTotalChunks(anyLong())).thenReturn(
					Optional.of(DEFAULT_TOTAL_CHUNKS));

				// WHEN & THEN
				assertThatThrownBy(() -> videoService.uploadVideoChunk(video.getId(), authMember, mismatchChunkRequest))
					.isInstanceOf(BadRequestException.class)
					.hasMessageContaining(TOTAL_CHUNKS_MISMATCH.getMessage());

				verify(videoUploadRedisRepository, never()).hasChunk(anyLong(), anyInt());
				verify(videoWriteService, never()).uploadVideoChunk(any(Video.class), any(UploadChunkRequest.class));
			}

			@Test
			@DisplayName("[❎ FAILURE] uploadVideoChunk: 이미 업로드된 청크를 재요청하여 비디오 청크 업로드에 실패했습니다.")
			void uploadVideoChunk_badRequestException_duplicateChunkUpload_fail() {
				// GIVEN
				final AuthMember authMember = AuthFixture.createAuthMember().build();
				final Member member = mock(Member.class);
				final Video video = mock(Video.class);
				final UploadChunkRequest uploadChunkRequest = VideoFixture.createUploadChunkRequest().build();

				when(member.getId()).thenReturn(authMember.id());
				when(video.getMember()).thenReturn(member);

				when(videoReadService.findVideoById(anyLong())).thenReturn(video);
				when(videoUploadRedisRepository.getTotalChunks(anyLong()))
					.thenReturn(Optional.of(DEFAULT_TOTAL_CHUNKS));
				when(videoUploadRedisRepository.hasChunk(anyLong(), anyInt())).thenReturn(true);

				// WHEN & THEN
				assertThatThrownBy(() -> videoService.uploadVideoChunk(video.getId(), authMember, uploadChunkRequest))
					.isInstanceOf(BadRequestException.class)
					.hasMessageContaining(DUPLICATE_CHUNK_UPLOAD.getMessage());

				verify(videoWriteService, never()).uploadVideoChunk(any(Video.class), any(UploadChunkRequest.class));
			}
		}
	}

	@Nested
	@DisplayName("추천 비디오 목록 조회 테스트: getRecommendedVideos")
	class GetRecommendedVideosTest {

		@Nested
		@DisplayName("추천 비디오 목록 조회 성공 테스트")
		class GetRecommendedVideosSuccess {
			@Test
			@DisplayName("[✅ SUCCESS] getRecommendedVideos: 인증된 사용자로 추천 비디오 목록 조회를 성공적으로 완료했습니다.")
			void getRecommendedVideos_videoResponse_success() {
				// GIVEN
				final AuthMember authMember = AuthFixture.createAuthMember().build();
				final List<VideoResponse> videoResponses = List.of(
					VideoFixture.createVideoResponse().videoId(1L).title("testVideoTitle1").build(),
					VideoFixture.createVideoResponse().videoId(2L).title("testVideoTitle2").build()
				);

				when(recommendationService.getRecommendedVideos(anyLong())).thenReturn(videoResponses);

				// WHEN
				final PagedResponse<VideoResponse> videoPagedResponse = videoService.getRecommendedVideosForUser(
					authMember);

				// THEN
				assertThat(videoPagedResponse.content()).isEqualTo(videoResponses);
				assertThat(videoPagedResponse.paginationInfo().pageNumber()).isEqualTo(1);
				assertThat(videoPagedResponse.paginationInfo().hasNext()).isFalse();
				verify(recommendationService, times(1)).getRecommendedVideos(authMember.id());
			}

			@Test
			@DisplayName("[✅ SUCCESS] getRecommendedVideos: 인증되지 않은 사용자로 빈 비디오 목록 조회를 성공적으로 완료했습니다.")
			void getRecommendedVideos_emptyList_success() {
				// GIVEN
				final AuthMember authMember = AuthFixture.createAuthMember().id(99L).build();
				final List<VideoResponse> emptyVideoResponses = List.of();

				when(recommendationService.getRecommendedVideos(anyLong())).thenReturn(emptyVideoResponses);

				// WHEN
				final PagedResponse<VideoResponse> videoPagedResponse = videoService.getRecommendedVideosForUser(
					authMember);

				// THEN
				assertThat(videoPagedResponse.content()).isEmpty();
				assertThat(videoPagedResponse.paginationInfo().pageNumber()).isEqualTo(1);
				assertThat(videoPagedResponse.paginationInfo().hasNext()).isFalse();
				verify(recommendationService, times(1)).getRecommendedVideos(authMember.id());
			}
		}

		@Nested
		@DisplayName("추천 비디오 목록 조회 실패 테스트")
		class GetRecommendedVideosFailure {

		}
	}
}

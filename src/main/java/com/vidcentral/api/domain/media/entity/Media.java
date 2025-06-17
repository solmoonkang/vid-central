package com.vidcentral.api.domain.media.entity;

import java.util.ArrayList;
import java.util.List;

import com.vidcentral.api.domain.media.model.MediaType;
import com.vidcentral.api.domain.member.entity.Member;
import com.vidcentral.api.dto.media.request.UploadMediaRequest;
import com.vidcentral.global.common.entity.BaseTimeEntity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "MEDIA")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Media extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "media_id")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id", nullable = false)
	private Member member;

	@Column(name = "title", length = 200)
	private String title;

	@Column(name = "content", columnDefinition = "TEXT")
	private String content;

	@Enumerated(EnumType.STRING)
	@Column(name = "media_type", nullable = false, length = 10)
	private MediaType mediaType;

	@OneToMany(mappedBy = "media", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Attachment> attachments = new ArrayList<>();

	private Media(Member member, String title, String content, MediaType mediaType) {
		this.member = member;
		this.title = title;
		this.content = content;
		this.mediaType = mediaType;
	}

	public static Media createMedia(Member member, UploadMediaRequest uploadMediaRequest) {
		return new Media(
			member,
			uploadMediaRequest.title(),
			uploadMediaRequest.content(),
			uploadMediaRequest.mediaType()
		);
	}
}

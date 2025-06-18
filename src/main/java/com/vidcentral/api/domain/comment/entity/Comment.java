package com.vidcentral.api.domain.comment.entity;

import com.vidcentral.api.domain.media.entity.Media;
import com.vidcentral.api.domain.member.entity.Member;
import com.vidcentral.api.dto.comment.request.CreateCommentRequest;
import com.vidcentral.global.common.entity.BaseTimeEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "COMMENTS")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "comment_id")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id", nullable = false)
	private Member member;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "media_id", nullable = false)
	private Media media;

	@Column(name = "content", nullable = false)
	private String content;

	private Comment(Member member, Media media, String content) {
		this.member = member;
		this.media = media;
		this.content = content;
	}

	public static Comment createComment(Member member, Media media, CreateCommentRequest createCommentRequest) {
		return new Comment(member, media, createCommentRequest.content());
	}
}

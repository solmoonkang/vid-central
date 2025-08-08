package com.vidcentral.api.domain.video.model;

public enum UploadStatus {

	// 클라이언트로부터 파일 업로드가 시작된 상태
	UPLOADING,

	// 파일 업로드가 완료되고, 서버에서 인코딩 등 후처리 작업이 진행 중인 상태
	PROCESSING,

	// 모든 후처리 작업이 완료되어 시청 가능한 상태
	COMPLETE,

	// 업로드 또는 후처리 작업 중 오류가 발생한 상태
	FAILED
}

package com.vidcentral.fixtures.history;

import com.vidcentral.api.domain.history.entity.ViewingHistory;
import com.vidcentral.api.domain.member.entity.Member;
import com.vidcentral.api.domain.video.entity.Video;

public class ViewingHistoryFixture {

	public static ViewingHistory createViewingHistoryEntity(Member member, Video video) {
		return ViewingHistory.createViewingHistory(member, video);
	}
}

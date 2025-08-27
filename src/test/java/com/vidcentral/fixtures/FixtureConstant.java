package com.vidcentral.fixtures;

import java.util.List;

import com.vidcentral.api.domain.video.model.Category;

public final class FixtureConstant {

	private FixtureConstant() {}

	public static final Long DEFAULT_ID = 1L;
	public static final String DEFAULT_EMAIL = "testMember@example.com";
	public static final String DEFAULT_NICKNAME = "testMember";
	public static final String DEFAULT_PASSWORD = "testPassword";

	public static final String UPDATED_NICKNAME = "updatedTestMember";
	public static final String UPDATED_INTRODUCE = "updatedTestIntroduce";

	public static final String DEFAULT_TITLE = "testVideoTitle";
	public static final String DEFAULT_DESCRIPTION = "testVideoDescription";
	public static final List<String> DEFAULT_RESOLUTIONS = List.of("480p", "720p", "1080p");
	public static final Category DEAFULT_CATEGORY = Category.ETC;
	public static final List<String> DEFAULT_TAGS = List.of("testTag1", "testTag2", "testTag3");
}

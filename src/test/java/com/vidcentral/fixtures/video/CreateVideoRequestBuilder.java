package com.vidcentral.fixtures.video;

import static com.vidcentral.fixtures.FixtureConstant.*;

import java.util.List;

import com.vidcentral.api.domain.video.model.Category;
import com.vidcentral.api.dto.video.request.CreateVideoRequest;

public class CreateVideoRequestBuilder {

	private String title = DEFAULT_TITLE;
	private String description = DEFAULT_DESCRIPTION;
	private List<String> resolutions = DEFAULT_RESOLUTIONS;
	private Category category = DEAFULT_CATEGORY;
	private List<String> tags = DEFAULT_TAGS;

	public CreateVideoRequestBuilder title(String title) {
		this.title = title;
		return this;
	}

	public CreateVideoRequestBuilder description(String description) {
		this.description = description;
		return this;
	}

	public CreateVideoRequestBuilder resolutions(List<String> resolutions) {
		this.resolutions = resolutions;
		return this;
	}

	public CreateVideoRequestBuilder category(Category category) {
		this.category = category;
		return this;
	}

	public CreateVideoRequestBuilder tags(List<String> tags) {
		this.tags = tags;
		return this;
	}

	public CreateVideoRequest build() {
		return new CreateVideoRequest(title, description, resolutions, category, tags);
	}
}

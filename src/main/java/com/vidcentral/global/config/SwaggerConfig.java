package com.vidcentral.global.config;

import static com.vidcentral.global.common.util.TokenConstant.*;
import static io.swagger.v3.oas.models.security.SecurityScheme.In.*;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.security.SecurityScheme.Type;

@Configuration
public class SwaggerConfig {

	private static final String OPEN_API_TITLE = "Media Platform API Documentation";
	private static final String OPEN_API_DESCRIPTION = "해당 API는 사용자 인증, 비디오 업로드, 댓글 기능 등을 제공합니다.";
	private static final String SECURITY_SCHEME_DESCRIPTION = "JWT 인증을 위한 BEARER 토큰입니다.";

	@Bean
	public OpenAPI swaggerOpenAPI() {
		final Info info = new Info().title(OPEN_API_TITLE).description(OPEN_API_DESCRIPTION);
		final SecurityRequirement securityRequirement = new SecurityRequirement().addList(AUTHORIZATION_HEADER);
		final SecurityScheme accessTokenSecurityScheme = getSecurityScheme();
		final Components components = new Components().addSecuritySchemes(BEARER_PREFIX, accessTokenSecurityScheme);

		return new OpenAPI().info(info).addSecurityItem(securityRequirement).components(components);
	}

	private SecurityScheme getSecurityScheme() {
		return new SecurityScheme()
			.name(AUTHORIZATION_HEADER)
			.type(Type.HTTP)
			.scheme(BEARER_PREFIX)
			.in(HEADER)
			.description(SECURITY_SCHEME_DESCRIPTION);
	}
}

package com.vidcentral.global.config;

import static org.springframework.http.HttpMethod.*;
import static org.springframework.security.config.http.SessionCreationPolicy.*;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import com.vidcentral.global.auth.filter.JwtAuthenticationFilter;
import com.vidcentral.global.auth.provider.JwtProvider;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

	private final JwtProvider jwtProvider;
	private final HandlerExceptionResolver handlerExceptionResolver;

	private static final String[] DEV_TOOLS_ENDPOINTS = {
		"/h2-console/**", "/v3/api-docs/**", "/swagger-ui/**", "/swagger-resources/**"
	};
	private static final String[] AUTHENTICATION_REQUEST_ENDPOINTS = {
		"/api/signup", "/api/login"
	};
	private static final String[] UNAUTHENTICATED_ACCESS_ENDPOINTS = {
		"/api/videos/recommend", "/api/videos/search", "/api/videos/{videoId}", "/api/videos/histories"
	};

	@Bean
	public WebSecurityCustomizer webSecurityCustomizer() {
		return web -> web.ignoring()
			.requestMatchers(PathRequest.toStaticResources().atCommonLocations())
			.requestMatchers(DEV_TOOLS_ENDPOINTS)
			.requestMatchers(POST, AUTHENTICATION_REQUEST_ENDPOINTS);
	}

	@Bean
	protected SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
		httpSecurity.csrf(AbstractHttpConfigurer::disable)
			.httpBasic(AbstractHttpConfigurer::disable)
			.sessionManagement(session -> session.sessionCreationPolicy(STATELESS));

		httpSecurity.authorizeHttpRequests(auth -> auth
			.requestMatchers(GET, UNAUTHENTICATED_ACCESS_ENDPOINTS).permitAll()
			.anyRequest().authenticated());

		httpSecurity.addFilterBefore(
			new JwtAuthenticationFilter(jwtProvider, handlerExceptionResolver),
			UsernamePasswordAuthenticationFilter.class);

		return httpSecurity.build();
	}

	@Bean
	public static PasswordEncoder passwordEncoder() {
		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}
}

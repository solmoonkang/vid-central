package com.vidcentral.global.config;

import static org.springframework.security.config.http.SessionCreationPolicy.*;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import com.vidcentral.global.auth.JwtProvider;
import com.vidcentral.global.auth.filter.AuthenticationFilter;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

	private final JwtProvider jwtProvider;
	private final HandlerExceptionResolver handlerExceptionResolver;

	private static final String[] AUTHENTICATION_REQUEST_ENDPOINTS = {
		"/api/signup",
		"/api/login"
	};
	private static final String[] MEMBER_INFO_ENDPOINTS = {
		"/api/members/**",
		"/api/videos/**",
		"/api/comments/**"
	};

	@Bean
	public WebSecurityCustomizer webSecurityCustomizer() {
		return web -> web.ignoring()
			.requestMatchers(PathRequest.toStaticResources().atCommonLocations())
			.requestMatchers("/h2-console/**")
			.requestMatchers("/v3/api-docs/**")
			.requestMatchers("/swagger-ui/**")
			.requestMatchers("/swagger-resources/**")
			.requestMatchers(HttpMethod.POST, AUTHENTICATION_REQUEST_ENDPOINTS);
	}

	@Bean
	protected SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
		httpSecurity.csrf(AbstractHttpConfigurer::disable)
			.httpBasic(AbstractHttpConfigurer::disable)
			.sessionManagement(session -> session.sessionCreationPolicy(STATELESS));

		httpSecurity.authorizeHttpRequests(auth -> auth
			.requestMatchers(HttpMethod.GET, MEMBER_INFO_ENDPOINTS).permitAll()
			.anyRequest().authenticated());

		httpSecurity.addFilterBefore(
			new AuthenticationFilter(jwtProvider, handlerExceptionResolver),
			UsernamePasswordAuthenticationFilter.class);

		return httpSecurity.build();
	}

	@Bean
	public static PasswordEncoder passwordEncoder() {
		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}
}

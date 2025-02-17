package com.vidcentral.global.auth.filter;

import static com.vidcentral.global.common.util.GlobalConstant.*;
import static com.vidcentral.global.common.util.TokenConstant.*;
import static com.vidcentral.global.error.model.ErrorMessage.*;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import com.vidcentral.api.application.auth.JwtProviderService;
import com.vidcentral.api.domain.auth.entity.AuthMember;
import com.vidcentral.global.auth.AuthenticationThreadLocal;
import com.vidcentral.global.common.util.CookieUtils;
import com.vidcentral.global.error.exception.NotFoundException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class AuthenticationFilter extends OncePerRequestFilter {

	private final JwtProviderService jwtProviderService;
	private final HandlerExceptionResolver handlerExceptionResolver;

	@Override
	protected void doFilterInternal(
		@NonNull HttpServletRequest httpServletRequest,
		@NonNull HttpServletResponse httpServletResponse,
		@NonNull FilterChain filterChain) {

		String accessToken = jwtProviderService.extractToken(ACCESS_TOKEN_HEADER, httpServletRequest);
		String refreshToken = CookieUtils.extractRefreshTokenFromCookies(httpServletRequest);

		try {
			if (jwtProviderService.isUsable(accessToken)) {
				setAuthenticate(accessToken);
				filterChain.doFilter(httpServletRequest, httpServletResponse);
				return;
			}

			if (jwtProviderService.isUsable(refreshToken)) {
				String newAccessToken = jwtProviderService.reGenerateToken(refreshToken, httpServletResponse);
				setAuthenticate(newAccessToken);
				filterChain.doFilter(httpServletRequest, httpServletResponse);
				return;
			}

			throw new NotFoundException(FAILED_TOKEN_NOT_FOUND_ERROR);
		} catch (Exception exception) {
			log.warn("[✅ LOGGER] JWT 에러 상세 설명: {}", exception.getMessage());
			httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			handlerExceptionResolver.resolveException(httpServletRequest, httpServletResponse, null, exception);
		} finally {
			AuthenticationThreadLocal.removeAuthMemberHolder();
		}
	}

	protected void setAuthenticate(String accessToken) {
		final AuthMember authMember = jwtProviderService.extractAuthMemberByAccessToken(accessToken);
		final Authentication authentication = new UsernamePasswordAuthenticationToken(authMember, BLANK, null);
		SecurityContextHolder.getContext().setAuthentication(authentication);
		AuthenticationThreadLocal.saveAuthMemberHolder(authMember);
	}
}

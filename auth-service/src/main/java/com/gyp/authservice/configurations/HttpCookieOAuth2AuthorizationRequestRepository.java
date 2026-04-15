package com.gyp.authservice.configurations;

import java.util.Arrays;
import java.util.Base64;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseCookie;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.util.SerializationUtils;

public class HttpCookieOAuth2AuthorizationRequestRepository implements AuthorizationRequestRepository<OAuth2AuthorizationRequest> {
	public static final String OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME = "oauth2_auth_request";
	public static final String REDIRECT_URI_PARAM_COOKIE_NAME = "oauth2_redirect_uri";
	private static final int COOKIE_EXPIRE_SECONDS = 180;

	@Override
	public OAuth2AuthorizationRequest loadAuthorizationRequest(HttpServletRequest request) {
		return getCookie(request, OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME)
				.map(cookie -> deserialize(cookie.getValue()))
				.orElse(null);
	}

	@Override
	public void saveAuthorizationRequest(OAuth2AuthorizationRequest authorizationRequest, HttpServletRequest request,
			HttpServletResponse response) {
		if(authorizationRequest == null) {
			removeAuthorizationRequestCookies(request, response);
			return;
		}

		addCookie(response, OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME, serialize(authorizationRequest), COOKIE_EXPIRE_SECONDS);
		String redirectUriAfterLogin = request.getParameter(REDIRECT_URI_PARAM_COOKIE_NAME);
		if(redirectUriAfterLogin != null && !redirectUriAfterLogin.isBlank()) {
			addCookie(response, REDIRECT_URI_PARAM_COOKIE_NAME, redirectUriAfterLogin, COOKIE_EXPIRE_SECONDS);
		}
	}

	@Override
	public OAuth2AuthorizationRequest removeAuthorizationRequest(HttpServletRequest request,
			HttpServletResponse response) {
		OAuth2AuthorizationRequest authorizationRequest = loadAuthorizationRequest(request);
		removeAuthorizationRequestCookies(request, response);
		return authorizationRequest;
	}

	public void removeAuthorizationRequestCookies(HttpServletRequest request, HttpServletResponse response) {
		deleteCookie(response, OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME);
		deleteCookie(response, REDIRECT_URI_PARAM_COOKIE_NAME);
	}

	private void addCookie(HttpServletResponse response, String name, String value, int maxAge) {
		ResponseCookie cookie = ResponseCookie.from(name, value)
				.path("/")
				.maxAge(maxAge)
				.httpOnly(true)
				.sameSite("Lax")
				.build();
		response.addHeader("Set-Cookie", cookie.toString());
	}

	private void deleteCookie(HttpServletResponse response, String name) {
		ResponseCookie cookie = ResponseCookie.from(name, "")
				.path("/")
				.maxAge(0)
				.httpOnly(true)
				.sameSite("Lax")
				.build();
		response.addHeader("Set-Cookie", cookie.toString());
	}

	private java.util.Optional<Cookie> getCookie(HttpServletRequest request, String name) {
		Cookie[] cookies = request.getCookies();
		if(cookies == null || cookies.length == 0) {
			return java.util.Optional.empty();
		}
		return Arrays.stream(cookies)
				.filter(cookie -> name.equals(cookie.getName()))
				.findFirst();
	}

	private String serialize(OAuth2AuthorizationRequest authorizationRequest) {
		return Base64.getUrlEncoder().encodeToString(SerializationUtils.serialize(authorizationRequest));
	}

	private OAuth2AuthorizationRequest deserialize(String value) {
		return (OAuth2AuthorizationRequest) SerializationUtils.deserialize(Base64.getUrlDecoder().decode(value));
	}
}
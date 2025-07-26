package com.gyp.common.utils;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.server.ResponseStatusException;

public final class SecurityUtils {
	private static final String USER_ID_PARAM = "userId";
	private static final String ORGANIZATION_ID_PARAM = "organizationId";

	public static String getCurrentUserId() {
		return getByParam(USER_ID_PARAM);
	}

	public static String getCurrentOrganizationId() {
		return getByParam(ORGANIZATION_ID_PARAM);
	}

	private static String getByParam(String paramName) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if(authentication == null || !authentication.isAuthenticated()) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User is not authenticated");
		}
		if(authentication instanceof JwtAuthenticationToken authenticationToken) {
			Object paramValue = authenticationToken.getTokenAttributes().get(paramName);
			if(paramValue != null) {
				return paramValue.toString();
			}
		}
		return null;
	}
}

package com.gyp.eventservice.utils;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public final class SecurityHolderUtils {
	private SecurityHolderUtils() {}

	public static String getCurrentUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		if (authentication != null && authentication.isAuthenticated()) {
			return authentication.getName();
		}

		return "anonymousUser";
	}
}

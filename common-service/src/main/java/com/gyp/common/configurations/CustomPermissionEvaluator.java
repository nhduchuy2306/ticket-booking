package com.gyp.common.configurations;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.gyp.common.enums.permission.ActionPermission;
import com.gyp.common.enums.permission.ApplicationPermission;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

@Component("permissionEvaluator")
public class CustomPermissionEvaluator implements PermissionEvaluator {
	@SuppressWarnings({ "unchecked" })
	@Override
	public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
		if(authentication == null || targetDomainObject == null || permission == null) {
			return false;
		}

		if(authentication instanceof JwtAuthenticationToken jwtAuthenticationToken) {
			var jwt = jwtAuthenticationToken.getTokenAttributes();
			Map<String, List<String>> items = (Map<String, List<String>>)jwt.get("permissions");

			if(items == null) {
				return false;
			}

			boolean isAdmin = items.entrySet().stream()
					.anyMatch(it -> ApplicationPermission.ADMIN.getApplicationId().equals(it.getKey()));
			if(isAdmin) {
				return true;
			}

			if(targetDomainObject instanceof ApplicationPermission applicationPermission
					&& permission instanceof ActionPermission actionPermission) {
				String appId = applicationPermission.getApplicationId();
				String action = actionPermission.name();

				List<String> actions = items.get(appId);
				if(actions == null || actions.isEmpty()) {
					return false;
				}
				return actions.contains(action);
			}
		}
		return false;
	}

	@Override
	public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType,
			Object permission) {
		return false;
	}
}

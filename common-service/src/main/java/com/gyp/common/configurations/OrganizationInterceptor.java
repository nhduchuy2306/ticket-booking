package com.gyp.common.configurations;

import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.gyp.common.contexts.OrganizationContext;
import com.gyp.common.entities.BaseOrganizationEntity;
import lombok.RequiredArgsConstructor;
import org.hibernate.Filter;
import org.hibernate.Session;
import org.springframework.http.server.PathContainer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.util.pattern.PathPatternParser;

@Component
@RequiredArgsConstructor
public class OrganizationInterceptor implements HandlerInterceptor {
	private static final List<String> PUBLIC_PATTERNS = List.of(
			"/organizations/register",
			"/events/on-sale",
			"/events/coming-events",
			"/events/{id}",
			"/sale-channels/active",
			"/sale-channels/by-slug/{orgSlug}",
			"/seat-maps/{id}",
			"/venues/{id}",
			"/seat-inventory/**"
	);

	private final EntityManager entityManager;
	private final PathPatternParser pathPatternParser = new PathPatternParser();

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
		if(shouldBypassFilter(request, handler)) {
			OrganizationContext.clear();
			return true;
		}

		String organizationId = resolveOrganizationId();
		OrganizationContext.setOrganizationId(organizationId);
		if(organizationId != null && !organizationId.isBlank()) {
			Session session = entityManager.unwrap(Session.class);
			Filter filter = session.enableFilter(BaseOrganizationEntity.ORGANIZATION_FILTER_NAME);
			filter.setParameter(BaseOrganizationEntity.ORGANIZATION_FILTER_PARAMETER, organizationId);
		}
		return true;
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
			Exception ex) {
		OrganizationContext.clear();
	}

	private String resolveOrganizationId() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if(!(authentication instanceof JwtAuthenticationToken jwtAuthenticationToken)) {
			return null;
		}
		Object organizationId = jwtAuthenticationToken.getTokenAttributes().get("organizationId");
		if(organizationId == null) {
			return null;
		}
		return organizationId.toString();
	}

	private boolean shouldBypassFilter(HttpServletRequest request, Object handler) {
		if(handler instanceof HandlerMethod handlerMethod) {
			String uri = request.getRequestURI();
			if(PUBLIC_PATTERNS.stream()
					.map(pathPatternParser::parse)
					.anyMatch(pathPattern -> pathPattern.matches(PathContainer.parsePath(uri)))) {
				return true;
			}
			if(handlerMethod.getBeanType().getName().contains("Controller")
					&& isAdminRequest()) {
				return true;
			}
		}
		return !OrganizationContext.hasOrganizationId() && resolveOrganizationId() == null;
	}

	private boolean isAdminRequest() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if(!(authentication instanceof JwtAuthenticationToken jwtAuthenticationToken)) {
			return false;
		}
		Object permissions = jwtAuthenticationToken.getTokenAttributes().get("permissions");
		return permissions instanceof java.util.Map<?, ?> permissionMap
				&& permissionMap.containsKey("app.admin");
	}
}


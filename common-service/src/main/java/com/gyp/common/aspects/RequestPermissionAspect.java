package com.gyp.common.aspects;

import java.lang.reflect.Method;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;

import com.gyp.common.annotations.RequestPermission;
import com.gyp.common.converters.Serialization;
import com.gyp.common.enums.permission.ApplicationPermission;
import com.gyp.common.exceptions.AccessDeniedException;
import com.gyp.common.jwt.JwtTokenProvider;
import com.nimbusds.jose.JOSEException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class RequestPermissionAspect {
	private static final String REQUEST_PERMISSION_AROUND_CONDITION = """
			@annotation(com.gyp.common.annotations.RequestPermissions) ||
			@annotation(com.gyp.common.annotations.RequestPermission)
			""";

	@Value("${jwt.secret.token}")
	private String jwtSecretToken;

	@Around(REQUEST_PERMISSION_AROUND_CONDITION)
	public Object checkPermission(ProceedingJoinPoint joinPoint) throws Throwable {
		validateToken();
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Method method = getMethodFromJoinPoint(joinPoint);
		RequestPermission[] annotations = method.getAnnotationsByType(RequestPermission.class);
		if(annotations.length > 0) {
			Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
			String role = authorities.stream().toList().getFirst().getAuthority().substring(6);
			if(ApplicationPermission.ADMIN.getApplicationId().equals(role)) {
				return joinPoint.proceed();
			}
			Map<String, List<String>> appIdAndActions = Serialization.deserializeFromString(role);
			boolean isValid = false;
			for(RequestPermission requestPermission : annotations) {
				String applicationId = requestPermission.application().getApplicationId();
				if(appIdAndActions.containsKey(applicationId)) {
					List<String> actions = appIdAndActions.get(applicationId);
					isValid = Arrays.stream(requestPermission.actions())
							.anyMatch(action -> actions.contains(action.name()));
				}
			}

			if(isValid) {
				return joinPoint.proceed();
			} else {
				throw new AccessDeniedException("Access denied");
			}
		} else {
			return joinPoint.proceed();
		}
	}

	private Method getMethodFromJoinPoint(ProceedingJoinPoint joinPoint) {
		try {
			String methodName = joinPoint.getSignature().getName();
			Class<?> targetClass = joinPoint.getTarget().getClass();
			Method[] methods = targetClass.getDeclaredMethods();

			for(Method method : methods) {
				if(method.getName().equals(methodName) &&
				   method.getParameterCount() == joinPoint.getArgs().length) {
					return method;
				}
			}
		} catch(Exception e) {
			throw new IllegalArgumentException("Failed to retrieve method from join point", e);
		}
		throw new IllegalArgumentException("Method not found in join point");
	}

	private void validateToken() throws ParseException, JOSEException {
		ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
		if(servletRequestAttributes != null) {
			HttpServletRequest request = servletRequestAttributes.getRequest();
			String token = getBearerToken(request);
			if(StringUtils.isNotEmpty(token) && !JwtTokenProvider.validateToken(token, jwtSecretToken)) {
				throw new AccessDeniedException("Access denied");
			}
		}
	}

	private String getBearerToken(HttpServletRequest request) {
		String token = request.getHeader(HttpHeaders.AUTHORIZATION);
		if(StringUtils.isNotEmpty(token) && token.startsWith("Bearer ")) {
			return token.substring(7);
		}
		return null;
	}
}

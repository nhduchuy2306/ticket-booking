package com.example.ticket.authservice.evaluator;

import java.lang.reflect.Method;
import java.text.ParseException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.example.common.converters.Serialization;
import com.example.common.permissions.ActionPermission;
import com.example.ticket.authservice.annotations.RequestPermission;
import com.example.ticket.authservice.exceptions.AccessDeniedException;
import com.example.ticket.authservice.services.JwtTokenProvider;
import com.fasterxml.jackson.core.type.TypeReference;
import com.nimbusds.jose.JOSEException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
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
public class RequestPermissionEvaluator {

	private final JwtTokenProvider jwtTokenProvider;

	@Around("@annotation(com.example.ticket.authservice.annotations.RequestPermission)")
	public Object checkPermission(ProceedingJoinPoint joinPoint)
			throws Throwable {
		validateToken();
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Method method = getMethodFromJoinPoint(joinPoint);
		RequestPermission annotation = method.getAnnotation(RequestPermission.class);
		if(annotation != null) {
			Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
			String role = authorities.stream().toList().get(0).getAuthority().substring(6);
			Map<String, List<String>> appIdAndActions = Serialization.deserializeFromString(role,
					new TypeReference<>() {});
			if(appIdAndActions.containsKey(annotation.application().getApplicationId())) {
				List<String> actions = appIdAndActions.get(annotation.application().getApplicationId());
				for(ActionPermission action : annotation.actions()) {
					if(!actions.contains(action.name())) {
						throw new AccessDeniedException("Permission denied");
					}
				}
				return joinPoint.proceed();
			} else {
				throw new AccessDeniedException("Permission denied");
			}
		}
		throw new RuntimeException("Annotation not found");
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
			throw new RuntimeException("Failed to retrieve method from join point", e);
		}
		throw new RuntimeException("Method not found in join point");
	}

	private void validateToken() throws ParseException, JOSEException {
		ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
		if(servletRequestAttributes != null) {
			HttpServletRequest request = servletRequestAttributes.getRequest();
			String token = getBearerToken(request);
			if(StringUtils.isNotEmpty(token) && !jwtTokenProvider.validateToken(token)) {
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

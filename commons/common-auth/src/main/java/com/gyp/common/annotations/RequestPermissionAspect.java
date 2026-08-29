package com.gyp.common.annotations;

import com.gyp.common.configurations.CustomPermissionEvaluator;
import com.gyp.common.exceptions.AccessDeniedException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class RequestPermissionAspect {
	private final CustomPermissionEvaluator permissionEvaluator;

	@Around("@annotation(requestPermission)")
	public Object around(ProceedingJoinPoint joinPoint, RequestPermission requestPermission) throws Throwable {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		boolean granted = permissionEvaluator.hasPermission(
				auth,
				requestPermission.application(),
				requestPermission.action()
		);

		if(!granted) {
			throw new AccessDeniedException("Access denied");
		}

		return joinPoint.proceed();
	}
}


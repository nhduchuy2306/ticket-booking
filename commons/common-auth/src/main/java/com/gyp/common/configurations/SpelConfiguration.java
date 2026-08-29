package com.gyp.common.configurations;

import java.util.function.Supplier;

import org.aopalliance.intercept.MethodInvocation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.core.Authentication;

@Configuration
public class SpelConfiguration {
	@Bean
	public MethodSecurityExpressionHandler methodSecurityExpressionHandler(CustomPermissionEvaluator evaluator) {
		var handler = new DefaultMethodSecurityExpressionHandler() {
			@Override
			public EvaluationContext createEvaluationContext(Supplier<Authentication> authentication,
					MethodInvocation methodInvocation) {
				StandardEvaluationContext context = (StandardEvaluationContext)super.createEvaluationContext(
						authentication, methodInvocation);
				context.setVariable("AppPerm", com.gyp.common.enums.permission.ApplicationPermission.class);
				context.setVariable("ActionPerm", com.gyp.common.enums.permission.ActionPermission.class);
				return context;
			}
		};
		handler.setPermissionEvaluator(evaluator);
		return handler;
	}
}

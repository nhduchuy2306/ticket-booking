package com.example.ticket.authservice.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.example.common.permissions.ActionPermission;
import com.example.common.permissions.ApplicationPermission;

@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface RequestPermission {
	ApplicationPermission application();

	ActionPermission[] actions() default {};
}

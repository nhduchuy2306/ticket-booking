package com.gyp.common.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.gyp.common.enums.permission.ActionPermission;
import com.gyp.common.enums.permission.ApplicationPermission;

@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface RequestPermission {
	ApplicationPermission application();

	ActionPermission action();
}

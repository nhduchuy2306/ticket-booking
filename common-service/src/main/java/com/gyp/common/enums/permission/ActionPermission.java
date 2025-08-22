package com.gyp.common.enums.permission;

import lombok.Getter;

@Getter
public enum ActionPermission {
	CREATE("create"),
	READ("read"),
	UPDATE("update"),
	DELETE("delete"),
	EXPORT("export"),
	IMPORT("import"),
	LOGIN("login"),
	GENERATE("generate"),
	LOGOUT("logout"),
	SYNC("sync");

	private final String actionName;

	ActionPermission(String actionName) {
		this.actionName = actionName;
	}

	public static ActionPermission fromValue(String value) {
		if(value == null) {
			return null;
		}

		return ActionPermission.valueOf(value);
	}
}

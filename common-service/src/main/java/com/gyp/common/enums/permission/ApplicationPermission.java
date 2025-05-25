package com.gyp.common.enums.permission;

import java.util.Arrays;
import java.util.List;

import lombok.Getter;

@Getter
public enum ApplicationPermission {
	ADMIN("app.admin", actions()),
	USER_GROUP("app.user.group",
			actions(ActionPermission.CREATE, ActionPermission.READ, ActionPermission.UPDATE, ActionPermission.DELETE)),
	USER_ACCOUNT("app.user.account",
			actions(ActionPermission.CREATE, ActionPermission.READ, ActionPermission.UPDATE, ActionPermission.DELETE,
					ActionPermission.LOGIN, ActionPermission.LOGOUT)),
	EVENT("app.event",
			actions(ActionPermission.CREATE, ActionPermission.READ, ActionPermission.UPDATE, ActionPermission.DELETE)),
	CONFIGURATION("app.configuration",
			actions(ActionPermission.CREATE, ActionPermission.READ, ActionPermission.UPDATE, ActionPermission.DELETE,
					ActionPermission.IMPORT, ActionPermission.EXPORT));

	private final String applicationId;
	private final List<ActionPermission> actionPermissions;

	ApplicationPermission(String applicationId, List<ActionPermission> actionPermissions) {
		this.applicationId = applicationId;
		this.actionPermissions = actionPermissions;
	}

	private static List<ActionPermission> actions(ActionPermission... actions) {
		return Arrays.asList(actions);
	}

	public static List<ApplicationPermission> getApplicationPermissions() {
		return Arrays.asList(ApplicationPermission.values());
	}
}

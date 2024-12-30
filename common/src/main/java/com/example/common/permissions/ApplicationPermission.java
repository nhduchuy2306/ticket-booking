package com.example.common.permissions;

import java.util.Arrays;
import java.util.List;

import lombok.Getter;

@Getter
public enum ApplicationPermission {
	MOVIE("app.movie",
			actions(ActionPermission.CREATE, ActionPermission.READ, ActionPermission.UPDATE, ActionPermission.DELETE)),
	THEATER("app.theater",
			actions(ActionPermission.CREATE, ActionPermission.READ, ActionPermission.UPDATE, ActionPermission.DELETE)),
	USER_GROUP("app.user.group",
			actions(ActionPermission.CREATE, ActionPermission.READ, ActionPermission.UPDATE, ActionPermission.DELETE)),
	USER("app.user",
			actions(ActionPermission.CREATE, ActionPermission.READ, ActionPermission.UPDATE, ActionPermission.DELETE));

	private final String applicationId;
	private final List<ActionPermission> actionPermissions;

	ApplicationPermission(String applicationId, List<ActionPermission> actionPermissions) {
		this.applicationId = applicationId;
		this.actionPermissions = actionPermissions;
	}

	private static List<ActionPermission> actions(ActionPermission... actions) {
		return Arrays.asList(actions);
	}
}

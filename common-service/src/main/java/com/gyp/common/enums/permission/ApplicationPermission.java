package com.gyp.common.enums.permission;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import lombok.Getter;

@Getter
public enum ApplicationPermission {
	ADMIN("Admin", "app.admin", actions()),
	USER_GROUP("User Group", "app.user.group",
			actions(ActionPermission.CREATE, ActionPermission.READ, ActionPermission.UPDATE, ActionPermission.DELETE)),
	USER_ACCOUNT("User Account", "app.user.account",
			actions(ActionPermission.CREATE, ActionPermission.READ, ActionPermission.UPDATE, ActionPermission.DELETE,
					ActionPermission.LOGIN, ActionPermission.LOGOUT, ActionPermission.SYNC)),
	ORGANIZATION("Organization", "app.organization",
			actions(ActionPermission.CREATE, ActionPermission.READ, ActionPermission.UPDATE, ActionPermission.DELETE)),
	EVENT("Event", "app.event",
			actions(ActionPermission.CREATE, ActionPermission.READ, ActionPermission.UPDATE, ActionPermission.DELETE,
					ActionPermission.SYNC)),
	EVENT_IMAGE("Event Image", "app.event.image",
			actions(ActionPermission.CREATE, ActionPermission.READ, ActionPermission.UPDATE, ActionPermission.DELETE)),
	CATEGORY("Category", "app.category",
			actions(ActionPermission.CREATE, ActionPermission.READ, ActionPermission.UPDATE, ActionPermission.DELETE)),
	SEASON("Season", "app.season",
			actions(ActionPermission.CREATE, ActionPermission.READ, ActionPermission.UPDATE, ActionPermission.DELETE)),
	SEAT_MAP("Seat Map", "app.seat.map",
			actions(ActionPermission.CREATE, ActionPermission.READ, ActionPermission.UPDATE, ActionPermission.DELETE,
					ActionPermission.IMPORT, ActionPermission.EXPORT)),
	TICKET_TYPE("Ticket Type", "app.ticket.type",
			actions(ActionPermission.CREATE, ActionPermission.READ, ActionPermission.UPDATE, ActionPermission.DELETE,
					ActionPermission.IMPORT, ActionPermission.EXPORT)),
	VENUE("Venue", "app.venue",
			actions(ActionPermission.CREATE, ActionPermission.READ, ActionPermission.UPDATE, ActionPermission.DELETE,
					ActionPermission.IMPORT, ActionPermission.EXPORT)),
	VENUE_MAP("Venue Map", "app.venue.map",
			actions(ActionPermission.CREATE, ActionPermission.READ, ActionPermission.UPDATE, ActionPermission.DELETE,
					ActionPermission.IMPORT, ActionPermission.EXPORT)),
	CONFIGURATION("Configuration", "app.configuration",
			actions(ActionPermission.CREATE, ActionPermission.READ, ActionPermission.UPDATE, ActionPermission.DELETE,
					ActionPermission.IMPORT, ActionPermission.EXPORT));

	private final String name;
	private final String applicationId;
	private final List<ActionPermission> actionPermissions;

	ApplicationPermission(String name, String applicationId, List<ActionPermission> actionPermissions) {
		this.applicationId = applicationId;
		this.actionPermissions = actionPermissions;
		this.name = name;
	}

	private static List<ActionPermission> actions(ActionPermission... actions) {
		return Arrays.asList(actions);
	}

	public static List<ApplicationPermission> getApplicationPermissions(boolean admin) {
		if(admin) {
			return Arrays.asList(ApplicationPermission.values());
		}
		return Arrays.stream(ApplicationPermission.values())
				.filter(permission -> !ApplicationPermission.ADMIN.applicationId.equals(permission.getApplicationId()))
				.collect(Collectors.toList());
	}
}

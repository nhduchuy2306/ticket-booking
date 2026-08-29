package com.gyp.common.kafkatopics;

public final class AuthServiceTopic {
	private AuthServiceTopic() {
	}

	private static final String AUTH_SERVICE_PREFIX = "auth";
	public static final String USER_ACCOUNT_SYNC = AUTH_SERVICE_PREFIX + ".user.account.sync";
	public static final String ORGANIZER_CREATE_COMMAND = AUTH_SERVICE_PREFIX + ".organizer.command.create";
	public static final String ORGANIZER_UPDATE_COMMAND = AUTH_SERVICE_PREFIX + ".organizer.command.create";
	public static final String ORGANIZER_DELETE_COMMAND = AUTH_SERVICE_PREFIX + ".organizer.command.create";
}

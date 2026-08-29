package com.gyp.common.enums.auth;

public enum RealmTypeEnum {
	GYP_DEFAULT_REALM("gyp-default"),
	GYP_KEYCLOAK_REALM("gyp-keycloak");

	private final String name;

	public String getName() {
		return name;
	}

	RealmTypeEnum(String name) {
		this.name = name;
	}
}

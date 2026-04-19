package com.gyp.common.contexts;

public final class OrganizationContext {
	private static final ThreadLocal<String> ORGANIZATION_ID = new ThreadLocal<>();

	private OrganizationContext() {
	}

	public static void setOrganizationId(String organizationId) {
		if(organizationId == null || organizationId.isBlank()) {
			ORGANIZATION_ID.remove();
			return;
		}
		ORGANIZATION_ID.set(organizationId);
	}

	public static String getOrganizationId() {
		return ORGANIZATION_ID.get();
	}

	public static boolean hasOrganizationId() {
		String organizationId = ORGANIZATION_ID.get();
		return organizationId != null && !organizationId.isBlank();
	}

	public static void clear() {
		ORGANIZATION_ID.remove();
	}
}


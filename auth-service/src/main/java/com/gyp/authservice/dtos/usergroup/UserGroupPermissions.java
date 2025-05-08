package com.gyp.authservice.dtos.usergroup;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class UserGroupPermissions implements Serializable {
	@Serial
	private static final long serialVersionUID = 1755542096600116308L;

	private List<PermissionItem> permissionItems = new ArrayList<>();

	public List<PermissionItem> getPermissionItems() {
		if(permissionItems == null) {
			permissionItems = new ArrayList<>();
		}
		return permissionItems;
	}

	public void setPermissionItems(List<PermissionItem> permissionItems) {
		this.permissionItems = new ArrayList<>(permissionItems);
	}
}

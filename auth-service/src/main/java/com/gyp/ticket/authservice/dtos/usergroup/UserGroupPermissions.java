package com.gyp.ticket.authservice.dtos.usergroup;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("usergrouppermissions")
@JsonTypeInfo(use = Id.NAME, include = As.WRAPPER_OBJECT)
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

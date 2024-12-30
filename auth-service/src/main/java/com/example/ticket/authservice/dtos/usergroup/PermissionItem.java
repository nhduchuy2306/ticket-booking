package com.example.ticket.authservice.dtos.usergroup;

import java.io.Serial;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import com.example.common.permissions.ActionPermission;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonTypeName("permissionitem")
@JsonTypeInfo(use = Id.NAME, include = As.WRAPPER_OBJECT)
public class PermissionItem implements Serializable {
	@Serial
	private static final long serialVersionUID = 4630508110484975320L;

	private Set<ActionPermission> actions = new HashSet<>();
	private String applicationId;
	private String uuid;
}

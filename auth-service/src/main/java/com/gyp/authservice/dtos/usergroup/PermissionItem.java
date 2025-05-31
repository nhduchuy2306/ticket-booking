package com.gyp.authservice.dtos.usergroup;

import java.io.Serial;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import com.gyp.common.enums.permission.ActionPermission;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PermissionItem implements Serializable {
	@Serial
	private static final long serialVersionUID = 4630508110484975320L;

	private Set<ActionPermission> actions = new HashSet<>();
	private String applicationId;
}

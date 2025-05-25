package com.gyp.authservice.dtos.usergroup;

import java.util.List;

import com.gyp.common.enums.permission.ActionPermission;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApplicationPermissionDto {
	private String applicationId;
	private List<ActionPermission> actionPermissions;
}

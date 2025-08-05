package com.gyp.authservice.dtos.usergroup;

import java.io.Serial;

import com.gyp.authservice.dtos.AbstractDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserGroupResponseDto extends AbstractDto {
	@Serial
	private static final long serialVersionUID = 1748359743573759399L;

	private String name;
	private String description;
	private Boolean administrator;
	private String organizationId;
	private UserGroupPermissions userGroupPermissions;
}

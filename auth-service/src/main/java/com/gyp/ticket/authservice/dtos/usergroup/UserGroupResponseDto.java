package com.gyp.ticket.authservice.dtos.usergroup;

import java.io.Serial;

import com.gyp.ticket.authservice.dtos.AbstractDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class UserGroupResponseDto extends AbstractDto {
	@Serial
	private static final long serialVersionUID = 1748359743573759399L;

	private String name;
	private String description;
	private Boolean administrator;
	private UserGroupPermissions userGroupPermissions;
}

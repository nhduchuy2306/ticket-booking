package com.gyp.ticket.authservice.dtos.usergroup;

import java.io.Serial;

import jakarta.validation.constraints.Size;

import com.gyp.common.types.PropertyLength;
import com.gyp.ticket.authservice.dtos.AbstractDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class UserGroupRequestDto extends AbstractDto {
	@Serial
	private static final long serialVersionUID = 5697492069064319966L;

	@Size(max = PropertyLength.NAME_LENGTH)
	private String name;

	@Size(max = PropertyLength.DESCRIPTION_LENGTH)
	private String description;

	private Boolean administrator;
	private UserGroupPermissions userGroupPermissions;
}

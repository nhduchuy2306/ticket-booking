package com.gyp.authservice.dtos.usergroup;

import java.io.Serial;

import jakarta.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.gyp.authservice.dtos.AbstractDto;
import com.gyp.common.intefaces.Request;
import com.gyp.common.types.PropertyLength;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@JsonTypeName("usergrouprequest")
@NoArgsConstructor
@AllArgsConstructor
public class UserGroupRequestDto extends AbstractDto implements Request {
	@Serial
	private static final long serialVersionUID = 5697492069064319966L;

	@Size(max = PropertyLength.NAME_LENGTH)
	private String name;

	@Size(max = PropertyLength.DESCRIPTION_LENGTH)
	private String description;

	private Boolean administrator;

	private String organizationId;

	private UserGroupPermissions userGroupPermissions;
}

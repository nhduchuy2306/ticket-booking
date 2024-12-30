package com.example.ticket.authservice.dtos.usergroup;

import java.io.Serial;
import java.util.UUID;

import com.example.ticket.authservice.dtos.AbstractDto;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@JsonTypeName("usergroupresponse")
@JsonTypeInfo(use = Id.NAME, include = As.WRAPPER_OBJECT)
public class UserGroupResponseDto extends AbstractDto {
	@Serial
	private static final long serialVersionUID = 1748359743573759399L;

	private String name;
	private String description;
	private Boolean administrator;
	private UserGroupPermissions userGroupPermissions;
}

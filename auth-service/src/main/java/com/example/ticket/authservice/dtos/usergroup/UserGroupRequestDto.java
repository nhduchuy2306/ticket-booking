package com.example.ticket.authservice.dtos.usergroup;

import java.io.Serial;
import java.util.UUID;

import com.example.common.types.PropertyLength;
import com.example.ticket.authservice.dtos.AbstractDto;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.fasterxml.jackson.annotation.JsonTypeName;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@JsonTypeName("usergrouprequest")
@JsonTypeInfo(use = Id.NAME, include = As.WRAPPER_OBJECT)
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

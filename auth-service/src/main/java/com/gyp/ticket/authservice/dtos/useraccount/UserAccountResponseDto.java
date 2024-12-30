package com.gyp.ticket.authservice.dtos.useraccount;

import java.io.Serial;
import java.time.LocalDateTime;
import java.util.List;

import com.gyp.ticket.authservice.dtos.AbstractDto;
import com.gyp.ticket.authservice.dtos.usergroup.UserGroupResponseDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class UserAccountResponseDto extends AbstractDto {
	@Serial
	private static final long serialVersionUID = -3267019645003048624L;

	private String name;
	private String username;
	private LocalDateTime dob;
	private String phoneNumber;
	private List<UserGroupResponseDto> userGroupResponseDtoList;
}

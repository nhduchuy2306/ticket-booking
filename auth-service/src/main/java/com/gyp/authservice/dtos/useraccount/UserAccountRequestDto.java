package com.gyp.authservice.dtos.useraccount;

import java.io.Serial;
import java.time.LocalDateTime;
import java.util.List;

import com.gyp.authservice.dtos.AbstractDto;
import com.gyp.authservice.dtos.usergroup.UserGroupRequestDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class UserAccountRequestDto extends AbstractDto {
	@Serial
	private static final long serialVersionUID = 195242380344331060L;

	private String name;
	private String username;
	private String password;
	private LocalDateTime dob;
	private String phoneNumber;
	private String email;
	private List<UserGroupRequestDto> userGroupRequestDtoList;
}

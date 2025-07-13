package com.gyp.authservice.dtos.useraccount;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserAccountRequestDto {
	@NotEmpty
	@NotNull
	private String name;

	@NotEmpty
	@NotNull
	private String username;

	@NotEmpty
	@NotNull
	private String password;
	private LocalDateTime dob;
	private String phoneNumber;

	@NotEmpty
	@NotNull
	private String email;
	private List<String> userGroupList;
}

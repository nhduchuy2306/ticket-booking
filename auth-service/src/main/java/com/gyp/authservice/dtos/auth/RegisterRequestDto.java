package com.gyp.authservice.dtos.auth;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequestDto {
	private String name;
	private String username;
	private String password;
	private String confirmPassword;
	private LocalDateTime dob;
	private String phoneNumber;
	private String email;
}

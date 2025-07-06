package com.gyp.authservice.dtos.auth;

import com.gyp.common.intefaces.Request;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequestDto implements Request {
	private String username;
	private String password;
}

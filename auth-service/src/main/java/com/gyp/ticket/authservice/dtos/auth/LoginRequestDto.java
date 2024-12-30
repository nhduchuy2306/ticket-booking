package com.gyp.ticket.authservice.dtos.auth;

import java.io.Serial;

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
	@Serial
	private static final long serialVersionUID = -7950125318397553044L;

	private String username;
	private String password;
}

package com.gyp.authservice.dtos.auth;

import java.io.Serial;

import com.gyp.common.intefaces.Response;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponseDto implements Response {
	@Serial
	private static final long serialVersionUID = 2120453009295682055L;

	private String token;
}

package com.gyp.authservice.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class SsoController {
	@GetMapping("sso/login")
	public String ssoLogin() {
		return "login";
	}
}

package com.example.ticket.authservice.controllers;

import com.example.common.permissions.ActionPermission;
import com.example.common.permissions.ApplicationPermission;
import com.example.ticket.authservice.annotations.RequestPermission;
import com.example.ticket.authservice.services.UserAccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(UserAccountController.USER_ACCOUNT_CONTROLLER_PATH)
public class UserAccountController extends AbstractController {
	public static final String USER_ACCOUNT_CONTROLLER_PATH = "useraccounts";

	private final UserAccountService userAccountService;

	@GetMapping
	@RequestPermission(application = ApplicationPermission.USER, actions = { ActionPermission.READ })
	public ResponseEntity<?> getUserAccountList() {
		var authentication = SecurityContextHolder.getContext().getAuthentication();
		log.info("User {} is getting user account list", authentication.getName());
		authentication.getAuthorities()
				.forEach(authority -> log.info("User {} has authority {}", authentication.getName(),
						authority.getAuthority()));

		return ResponseEntity.ok(userAccountService.getUserAccountList());
	}
}

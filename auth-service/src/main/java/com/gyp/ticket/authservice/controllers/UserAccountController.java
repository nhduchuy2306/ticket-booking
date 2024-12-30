package com.gyp.ticket.authservice.controllers;

import com.gyp.common.annotations.RequestPermission;
import com.gyp.common.controllers.AbstractController;
import com.gyp.common.enums.permission.ActionPermission;
import com.gyp.common.enums.permission.ApplicationPermission;
import com.gyp.ticket.authservice.services.UserAccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
	@RequestPermission(application = ApplicationPermission.USER_ACCOUNT, actions = { ActionPermission.READ })
	@RequestPermission(application = ApplicationPermission.USER_GROUP, actions = { ActionPermission.READ })
	public ResponseEntity<?> getUserAccountList() {
		return createResponseOk(userAccountService.getUserAccountList());
	}

	@GetMapping("/{id}")
	@RequestPermission(application = ApplicationPermission.USER_ACCOUNT, actions = { ActionPermission.READ })
	@RequestPermission(application = ApplicationPermission.USER_GROUP, actions = { ActionPermission.READ })
	public ResponseEntity<?> getUserAccountById(@PathVariable("id") String id) {
		if(StringUtils.isEmpty(id)) {
			return ResponseEntity.badRequest().body("Id is required");
		}
		return createResponseOk(userAccountService.getUserAccountById(id));
	}
}

package com.gyp.authservice.controllers;

import com.gyp.common.annotations.RequestPermission;
import com.gyp.common.controllers.AbstractController;
import com.gyp.common.enums.permission.ActionPermission;
import com.gyp.common.enums.permission.ApplicationPermission;
import com.gyp.authservice.services.UserGroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(UserGroupController.USER_GROUP_CONTROLLER_PATH)
@RequiredArgsConstructor
public class UserGroupController extends AbstractController {

	public static final String USER_GROUP_CONTROLLER_PATH = "usergroups";

	private final UserGroupService userGroupService;

	@GetMapping
	@RequestPermission(application = ApplicationPermission.USER_GROUP, actions = { ActionPermission.READ })
	public ResponseEntity<?> getListUserGroup() {
		return createResponseOk(userGroupService.getListUserGroups());
	}
}

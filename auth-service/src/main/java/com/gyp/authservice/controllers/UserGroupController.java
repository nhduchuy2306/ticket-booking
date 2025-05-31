package com.gyp.authservice.controllers;

import com.gyp.authservice.dtos.usergroup.UserGroupRequestDto;
import com.gyp.authservice.services.UserGroupService;
import com.gyp.common.annotations.RequestPermission;
import com.gyp.common.controllers.AbstractController;
import com.gyp.common.enums.permission.ActionPermission;
import com.gyp.common.enums.permission.ApplicationPermission;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(UserGroupController.USER_GROUP_CONTROLLER_PATH)
@RequiredArgsConstructor
public class UserGroupController extends AbstractController {
	public static final String USER_GROUP_CONTROLLER_PATH = "usergroups";

	private static final String USER_GROUP_ACTION_LIST_PATH = "/usergroupactions";

	private final UserGroupService userGroupService;

	@GetMapping
	@RequestPermission(application = ApplicationPermission.USER_GROUP, actions = { ActionPermission.READ })
	public ResponseEntity<?> getListUserGroup() {
		return createResponseOk(userGroupService.getListUserGroups());
	}

	@GetMapping(USER_GROUP_ACTION_LIST_PATH)
	@RequestPermission(application = ApplicationPermission.USER_GROUP, actions = { ActionPermission.READ })
	public ResponseEntity<?> getUserGroupList() {
		return createResponseOk(userGroupService.getListApplicationPermissions());
	}

	@PostMapping
	@RequestPermission(application = ApplicationPermission.USER_GROUP, actions = { ActionPermission.CREATE })
	public ResponseEntity<?> createUserGroup(@RequestBody UserGroupRequestDto userGroupRequestDto) {
		var res = userGroupService.createUserGroup(userGroupRequestDto);
		return ResponseEntity.ok(res);
	}

	@PutMapping("/{" + ID_PARAM + "}")
	@RequestPermission(application = ApplicationPermission.USER_GROUP, actions = { ActionPermission.UPDATE })
	public ResponseEntity<?> updateUserGroup(@PathVariable(ID_PARAM) String id,
			@RequestBody UserGroupRequestDto userGroupRequestDto) {
		var res = userGroupService.updateUserGroup(userGroupRequestDto, id);
		return ResponseEntity.ok(res);
	}

	@DeleteMapping("/{" + ID_PARAM + "}")
	@RequestPermission(application = ApplicationPermission.USER_GROUP, actions = { ActionPermission.DELETE })
	public ResponseEntity<?> deleteUserGroup(@PathVariable(ID_PARAM) String id) {
		try {
			var res = userGroupService.deleteUserGroup(id);
			return ResponseEntity.ok(res);
		} catch(Exception e) {
			return ResponseEntity.internalServerError().body(e.getMessage());
		}
	}
}

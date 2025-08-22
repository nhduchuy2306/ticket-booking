package com.gyp.authservice.controllers;

import java.util.Optional;

import com.gyp.authservice.dtos.usergroup.UserGroupRequestDto;
import com.gyp.authservice.services.UserGroupService;
import com.gyp.authservice.services.criteria.UserGroupSearchCriteria;
import com.gyp.common.controllers.AbstractController;
import com.gyp.common.dtos.pagination.PaginatedDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(UserGroupController.USER_GROUP_CONTROLLER_PATH)
@RequiredArgsConstructor
public class UserGroupController extends AbstractController {
	public static final String USER_GROUP_CONTROLLER_PATH = "usergroups";

	private static final String USER_GROUP_ACTION_LIST_PATH = "/usergroupactions";

	private final UserGroupService userGroupService;

	@GetMapping
	@PreAuthorize("@permissionEvaluator.hasPermission(authentication, #AppPerm.USER_GROUP, #ActionPerm.READ)")
	public ResponseEntity<?> getListUserGroup(
			@RequestParam(value = "sortBy", required = false) String sortBy,
			@RequestParam(value = "page", required = false) Optional<Integer> page,
			@RequestParam(value = "size", required = false) Optional<Integer> size) {
		String organizationId = getCurrentOrganizationId();
		UserGroupSearchCriteria userGroupSearchCriteria = UserGroupSearchCriteria.builder()
				.organizationId(organizationId)
				.sortBy(sortBy)
				.build();
		PaginatedDto pagination = PaginatedDto.builder()
				.page(page.orElse(0))
				.size(size.orElse(10))
				.build();
		return createResponseOk(userGroupService.getListUserGroups(userGroupSearchCriteria, pagination));
	}

	@GetMapping("/{" + ID_PARAM + "}")
	@PreAuthorize("@permissionEvaluator.hasPermission(authentication, #AppPerm.USER_GROUP, #ActionPerm.READ)")
	public ResponseEntity<?> getUserGroupById(@PathVariable(ID_PARAM) String id) {
		var res = userGroupService.getUserGroupById(id);
		if(res == null) {
			return ResponseEntity.notFound().build();
		}
		return createResponseOk(res);
	}

	@GetMapping(USER_GROUP_ACTION_LIST_PATH)
	@PreAuthorize("@permissionEvaluator.hasPermission(authentication, #AppPerm.USER_GROUP, #ActionPerm.READ)")
	public ResponseEntity<?> getUserGroupList() {
		return createResponseOk(userGroupService.getListApplicationPermissions());
	}

	@PostMapping
	@PreAuthorize("@permissionEvaluator.hasPermission(authentication, #AppPerm.USER_GROUP, #ActionPerm.CREATE)")
	public ResponseEntity<?> createUserGroup(@RequestBody UserGroupRequestDto userGroupRequestDto) {
		var res = userGroupService.createUserGroup(userGroupRequestDto);
		return ResponseEntity.ok(res);
	}

	@PutMapping("/{" + ID_PARAM + "}")
	@PreAuthorize("@permissionEvaluator.hasPermission(authentication, #AppPerm.USER_GROUP, #ActionPerm.UPDATE)")
	public ResponseEntity<?> updateUserGroup(@PathVariable(ID_PARAM) String id,
			@RequestBody UserGroupRequestDto userGroupRequestDto) {
		var res = userGroupService.updateUserGroup(userGroupRequestDto, id);
		return ResponseEntity.ok(res);
	}

	@DeleteMapping("/{" + ID_PARAM + "}")
	@PreAuthorize("@permissionEvaluator.hasPermission(authentication, #AppPerm.USER_GROUP, #ActionPerm.DELETE)")
	public ResponseEntity<?> deleteUserGroup(@PathVariable(ID_PARAM) String id) {
		try {
			var res = userGroupService.deleteUserGroup(id);
			return ResponseEntity.ok(res);
		} catch(Exception e) {
			return ResponseEntity.internalServerError().body(e.getMessage());
		}
	}
}

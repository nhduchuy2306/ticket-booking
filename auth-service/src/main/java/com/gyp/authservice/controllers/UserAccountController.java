package com.gyp.authservice.controllers;

import java.util.Optional;

import com.gyp.authservice.dtos.useraccount.UserAccountRequestDto;
import com.gyp.authservice.messages.producers.UserAccountProducer;
import com.gyp.authservice.services.UserAccountService;
import com.gyp.authservice.services.criteria.UserAccountSearchCriteria;
import com.gyp.common.controllers.AbstractController;
import com.gyp.common.dtos.pagination.PaginatedDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(UserAccountController.USER_ACCOUNT_CONTROLLER_PATH)
public class UserAccountController extends AbstractController {
	public static final String USER_ACCOUNT_CONTROLLER_PATH = "useraccounts";

	private static final String SYNC_ORGANIZER_PATH = "syncorganizer";

	private final UserAccountService userAccountService;
	private final UserAccountProducer userAccountProducer;

	@GetMapping
	@PreAuthorize(
			"@permissionEvaluator.hasPermission(authentication, #AppPerm.USER_ACCOUNT.getApplicationId(), #ActionPerm.READ.name()) or "
					+ "@permissionEvaluator.hasPermission(authentication, #AppPerm.USER_GROUP.getApplicationId(), #ActionPerm.READ.name())")
	public ResponseEntity<?> getListUserAccount(
			@RequestParam(value = "sortBy", required = false) String sortBy,
			@RequestParam(value = "page", required = false) Optional<Integer> page,
			@RequestParam(value = "size", required = false) Optional<Integer> size) {
		String organizationId = getCurrentOrganizationId();
		UserAccountSearchCriteria userGroupSearchCriteria = UserAccountSearchCriteria.builder()
				.organizationId(organizationId)
				.sortBy(sortBy)
				.build();
		PaginatedDto pagination = PaginatedDto.builder()
				.page(page.orElse(0))
				.size(size.orElse(10))
				.build();
		return createResponseOk(userAccountService.getUserAccountList(userGroupSearchCriteria, pagination));
	}

	@GetMapping("/{" + ID_PARAM + "}")
	@PreAuthorize(
			"@permissionEvaluator.hasPermission(authentication, #AppPerm.USER_ACCOUNT.getApplicationId(), #ActionPerm.READ.name()) or "
					+ "@permissionEvaluator.hasPermission(authentication, #AppPerm.USER_GROUP.getApplicationId(), #ActionPerm.READ.name())")
	public ResponseEntity<?> getUserAccountById(@PathVariable(ID_PARAM) String id) {
		if(StringUtils.isEmpty(id)) {
			return ResponseEntity.badRequest().body("Id is required");
		}
		return createResponseOk(userAccountService.getUserAccountById(id));
	}

	@PostMapping
	@PreAuthorize(
			"@permissionEvaluator.hasPermission(authentication, #AppPerm.USER_ACCOUNT.getApplicationId(), #ActionPerm.CREATE.name()) or "
					+ "@permissionEvaluator.hasPermission(authentication, #AppPerm.USER_GROUP.getApplicationId(), #ActionPerm.CREATE.name())")
	public ResponseEntity<?> createUserAccount(@RequestBody UserAccountRequestDto request) {
		var response = userAccountService.createUserAccount(request);
		return createResponseOk(response);
	}

	@PutMapping("/{" + ID_PARAM + "}")
	@PreAuthorize(
			"@permissionEvaluator.hasPermission(authentication, #AppPerm.USER_ACCOUNT.getApplicationId(), #ActionPerm.CREATE.name()) or "
					+ "@permissionEvaluator.hasPermission(authentication, #AppPerm.USER_GROUP.getApplicationId(), #ActionPerm.CREATE.name())")
	public ResponseEntity<?> createUserAccount(
			@PathVariable(ID_PARAM) String id,
			@RequestBody UserAccountRequestDto request
	) {
		var response = userAccountService.updateUserAccount(id, request);
		return createResponseOk(response);
	}

	@DeleteMapping("/{" + ID_PARAM + "}")
	@PreAuthorize(
			"@permissionEvaluator.hasPermission(authentication, #AppPerm.USER_ACCOUNT.getApplicationId(), #ActionPerm.DELETE.name()) or "
					+ "@permissionEvaluator.hasPermission(authentication, #AppPerm.USER_GROUP.getApplicationId(), #ActionPerm.DELETE.name())")
	public ResponseEntity<?> deleteUserAccount(@PathVariable(ID_PARAM) String id) {
		var response = userAccountService.deleteUserAccount(id);
		return createResponseOk(response);
	}

	@GetMapping(SYNC_ORGANIZER_PATH)
	@PreAuthorize(
			"@permissionEvaluator.hasPermission(authentication, #AppPerm.USER_ACCOUNT.getApplicationId(), #ActionPerm.SYNC.name()) or "
					+ "@permissionEvaluator.hasPermission(authentication, #AppPerm.USER_GROUP.getApplicationId(), #ActionPerm.READ.name())")
	public ResponseEntity<?> syncOrganizer() {
		userAccountProducer.syncUserAccount();
		return createResponseOk("Sync Organizer!!");
	}
}

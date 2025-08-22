package com.gyp.authservice.controllers;

import com.gyp.authservice.dtos.organization.OrganizationRequestDto;
import com.gyp.authservice.services.OrganizationService;
import com.gyp.common.controllers.AbstractController;
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
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(OrganizationController.ORGANIZATIONS_CONTROLLER_PATH)
public class OrganizationController extends AbstractController {
	public static final String ORGANIZATIONS_CONTROLLER_PATH = "/organizations";

	private final OrganizationService organizationService;

	@GetMapping
	@PreAuthorize("@permissionEvaluator.hasPermission(authentication, #AppPerm.ORGANIZATION, #ActionPerm.READ)")
	public ResponseEntity<?> getAllOrganizations() {
		return ResponseEntity.ok(organizationService.getAllOrganizations());
	}

	@GetMapping("/{" + ID_PARAM + "}")
	@PreAuthorize("@permissionEvaluator.hasPermission(authentication, #AppPerm.ORGANIZATION, #ActionPerm.READ)")
	public ResponseEntity<?> getOrganizationById(@PathVariable(ID_PARAM) String id) {
		return ResponseEntity.ok(organizationService.getOrganizationById(id));
	}

	@PostMapping
	@PreAuthorize("@permissionEvaluator.hasPermission(authentication, #AppPerm.ORGANIZATION, #ActionPerm.CREATE)")
	public ResponseEntity<?> createOrganization(@RequestBody OrganizationRequestDto organizationRequestDto) {
		return ResponseEntity.ok(organizationService.createOrganization(organizationRequestDto));
	}

	@PutMapping("/{" + ID_PARAM + "}")
	@PreAuthorize("@permissionEvaluator.hasPermission(authentication, #AppPerm.ORGANIZATION, #ActionPerm.UPDATE)")
	public ResponseEntity<?> updateOrganization(@PathVariable(ID_PARAM) String id,
			@RequestBody OrganizationRequestDto organizationRequestDto) {
		return ResponseEntity.ok(organizationService.updateOrganization(id, organizationRequestDto));
	}

	@DeleteMapping("/{" + ID_PARAM + "}")
	@PreAuthorize("@permissionEvaluator.hasPermission(authentication, #AppPerm.ORGANIZATION, #ActionPerm.DELETE)")
	public ResponseEntity<?> deleteOrganization(@PathVariable(ID_PARAM) String id) {
		organizationService.deleteOrganization(id);
		return ResponseEntity.noContent().build();
	}
}

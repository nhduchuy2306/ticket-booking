package com.gyp.authservice.clients;

import java.util.Map;

import com.gyp.authservice.configurations.FeignClientConfiguration;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
		name = "keycloak-admin-service",
		url = "${keycloak.admin.url}",
		configuration = FeignClientConfiguration.class
)
public interface KeyCloakAdminServiceClient {
	@PostMapping(value = "/realms/master/protocol/openid-connect/token",
			consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	Map<String, Object> getToken(@RequestBody MultiValueMap<String, String> formData);

	@PostMapping(value = "/admin/realms/gyp/roles", consumes = MediaType.APPLICATION_JSON_VALUE)
	Map<String, Object> createRole(@RequestBody Map<String, Object> body);

	@GetMapping(value = "/admin/realms/gyp/roles", consumes = MediaType.APPLICATION_JSON_VALUE)
	Map<String, Object> getRoles();

	@GetMapping(value = "/admin/realms/gyp/roles/{roleName}", consumes = MediaType.APPLICATION_JSON_VALUE)
	Map<String, Object> getRoleByName(@PathVariable("roleName") String roleName);

	@GetMapping(value = "/admin/realms/gyp/groups", consumes = MediaType.APPLICATION_JSON_VALUE)
	Map<String, Object> getAllGroups();

	@PostMapping(value = "/admin/realms/gyp/groups", consumes = MediaType.APPLICATION_JSON_VALUE)
	Map<String, Object> createGroup(@RequestBody Map<String, Object> body);

	@PutMapping(value = "/admin/realms/gyp/groups/{groupId}", consumes = MediaType.APPLICATION_JSON_VALUE)
	void updateGroup(@PathVariable("groupId") String groupId, @RequestBody Map<String, Object> body);
}

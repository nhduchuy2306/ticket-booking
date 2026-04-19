package com.gyp.authservice.dtos.organization;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import com.gyp.common.types.PropertyLength;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrganizationRequestDto {
	@NotBlank
	@Size(max = PropertyLength.NAME_LENGTH)
	private String orgName;

	@NotBlank
	@Pattern(regexp = "^[a-z0-9-]+$", message = "orgSlug must contain only lowercase letters, numbers, and hyphens")
	@Size(max = 100)
	private String orgSlug;

	@NotBlank
	@Email
	private String businessEmail;

	@NotBlank
	private String phone;

	@NotBlank
	private String address;

	@NotBlank
	private String taxCode;

	@NotBlank
	private String representativeName;

	private String representativePhone;

	@NotBlank
	@Email
	private String ownerEmail;

	@NotBlank
	private String ownerFullName;

	@NotBlank
	@Size(min = 8, max = 128)
	private String ownerPassword;

	@NotBlank
	@Size(min = 8, max = 128)
	private String confirmPassword;
}

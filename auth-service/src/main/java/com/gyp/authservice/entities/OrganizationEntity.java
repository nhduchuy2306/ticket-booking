package com.gyp.authservice.entities;

import java.io.Serial;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gyp.common.entities.AbstractEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Builder
@Table(name = "ORGANIZATION")
@NoArgsConstructor
@AllArgsConstructor
public class OrganizationEntity extends AbstractEntity {
	@Serial
	private static final long serialVersionUID = -3879729094234186602L;

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(name = "id")
	private String id;

	@Column(name = "name")
	private String name;

	@Column(name = "description")
	private String description;

	@Column(name = "org_slug", unique = true)
	private String orgSlug;

	@Column(name = "business_email", unique = true)
	private String businessEmail;

	@Column(name = "phone")
	private String phone;

	@Column(name = "address")
	private String address;

	@Column(name = "tax_code", unique = true)
	private String taxCode;

	@Column(name = "representative_name")
	private String representativeName;

	@Column(name = "representative_phone")
	private String representativePhone;

	@Column(name = "status")
	private String status;

	@JsonIgnore
	@OneToMany(mappedBy = "organizationEntity", fetch = FetchType.LAZY)
	private List<UserAccountEntity> userAccountEntityList = new ArrayList<>();

	@JsonIgnore
	@OneToMany(mappedBy = "organizationEntity", fetch = FetchType.LAZY)
	private List<UserGroupEntity> userGroupEntityList = new ArrayList<>();
}

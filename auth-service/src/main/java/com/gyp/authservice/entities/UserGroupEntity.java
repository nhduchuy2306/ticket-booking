package com.gyp.authservice.entities;

import java.io.Serial;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Builder
@Table(name = "USERGROUP")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class UserGroupEntity extends OrganizationScopedEntity {
	@Serial
	private static final long serialVersionUID = -7431399635614803652L;

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(name = "id")
	private String id;

	@Column(name = "name")
	private String name;

	@Column(name = "description")
	private String description;

	@Column(name = "administrator")
	private Boolean administrator;

	@Lob
	@Column(name = "user_group_permissions", columnDefinition = "text")
	private String userGroupPermissionsRaw;

	@JsonIgnore
	@ManyToMany(mappedBy = "userGroupEntityList", fetch = FetchType.LAZY)
	private List<UserAccountEntity> userAccountEntityList;
}

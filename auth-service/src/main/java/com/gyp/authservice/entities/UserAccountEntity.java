package com.gyp.authservice.entities;

import java.io.Serial;
import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import com.gyp.common.enums.auth.RealmTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@Entity
@SuperBuilder
@Table(name = "USERACCOUNT")
@NoArgsConstructor
@AllArgsConstructor
public class UserAccountEntity extends AbstractEntity {
	@Serial
	private static final long serialVersionUID = 5877294560019573626L;

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(name = "id")
	private String id;

	@Column(name = "name")
	private String name;

	@Column(name = "user_name", unique = true)
	private String username;

	@Column(name = "password")
	private String password;

	@Column(name = "dob")
	private LocalDateTime dob;

	@Column(name = "phone_number")
	private String phoneNumber;

	@Column(name = "email", unique = true)
	private String email;

	@Column(name = "realm_type")
	@Enumerated(EnumType.STRING)
	private RealmTypeEnum realmType;

	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinTable(
			name = "userpermissions",
			joinColumns = @JoinColumn(name = "user_account_id"),
			inverseJoinColumns = @JoinColumn(name = "user_group_id")
	)
	private List<UserGroupEntity> userGroupEntityList;

	@ManyToOne
	@JoinColumn(name = "organization_id")
	private OrganizationEntity organizationEntity;
}

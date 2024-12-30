package com.example.ticket.authservice.entities;

import java.io.Serial;
import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Builder
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "USERACCOUNT")
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

	@Column(name = "user_name")
	private String username;

	@Column(name = "password")
	private String password;

	@Column(name = "dob")
	private LocalDateTime dob;

	@Column(name = "phone_number")
	private String phoneNumber;

	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(
			name = "userpermissions",
			joinColumns = @JoinColumn(name = "user_account_id"),
			inverseJoinColumns = @JoinColumn(name = "user_group_id")
	)
	private List<UserGroupEntity> userGroupEntityList;

	public UserAccountEntity() {
		super();
	}
}

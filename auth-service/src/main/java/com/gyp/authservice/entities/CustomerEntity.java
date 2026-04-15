package com.gyp.authservice.entities;

import java.io.Serial;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@Entity
@SuperBuilder
@Table(name = "customer")
@NoArgsConstructor
@AllArgsConstructor
public class CustomerEntity extends AbstractEntity {

	@Serial
	private static final long serialVersionUID = 7434787912691706721L;

	@Id
	@Column(name = "id")
	private String id;

	@Column(name = "name")
	private String name;

	@Column(name = "email", unique = true)
	private String email;

	@Column(name = "phone_number")
	private String phoneNumber;

	@Column(name = "dob")
	private LocalDateTime dob;

	@Column(name = "provider", nullable = false)
	@Builder.Default
	private String provider = "local";

	@Column(name = "provider_id")
	private String providerId;

	@Column(name = "password")
	private String password;
}
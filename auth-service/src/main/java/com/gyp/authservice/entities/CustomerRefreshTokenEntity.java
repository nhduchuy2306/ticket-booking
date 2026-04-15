package com.gyp.authservice.entities;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Builder
@Table(name = "customerrefreshtoken")
@NoArgsConstructor
@AllArgsConstructor
public class CustomerRefreshTokenEntity implements Serializable {

	@Serial
	private static final long serialVersionUID = -5483185420111875216L;

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(name = "id")
	private String id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "customer_id", nullable = false)
	private CustomerEntity customer;

	@Column(name = "token", nullable = false, unique = true, length = 500)
	private String token;

	@Column(name = "expiry", nullable = false)
	private LocalDateTime expiry;

	@Column(name = "revoked")
	@Builder.Default
	private Boolean revoked = Boolean.FALSE;
}
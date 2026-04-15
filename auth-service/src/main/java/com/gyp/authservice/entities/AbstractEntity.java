package com.gyp.authservice.entities;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

import jakarta.persistence.MappedSuperclass;

import com.gyp.common.intefaces.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@MappedSuperclass
@NoArgsConstructor
public abstract class AbstractEntity implements Entity, Serializable {
	@Serial
	private static final long serialVersionUID = -7007252282678067980L;

	private String createUser;
	private String changeUser;
	private LocalDateTime createTimestamp;
	private LocalDateTime changeTimestamp;
}

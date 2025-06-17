package com.gyp.eventservice.entities;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;

import com.gyp.common.intefaces.Entity;
import lombok.Data;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Data
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class AbstractEntity implements Entity, Serializable {
	@Serial
	private static final long serialVersionUID = -7007252282678067980L;

	private String createUser;
	private String changeUser;
	private LocalDateTime createTimestamp;
	private LocalDateTime changeTimestamp;
}

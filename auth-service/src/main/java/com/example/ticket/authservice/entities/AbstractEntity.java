package com.example.ticket.authservice.entities;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

import jakarta.persistence.MappedSuperclass;
import lombok.Data;

@Data
@MappedSuperclass
public abstract class AbstractEntity implements Serializable {
	@Serial
	private static final long serialVersionUID = -7007252282678067980L;

	private String createUser;
	private String changeUser;
	private LocalDateTime createTimestamp;
	private LocalDateTime changeTimestamp;
}

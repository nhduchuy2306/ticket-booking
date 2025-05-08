package com.gyp.authservice.dtos;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

import com.gyp.common.intefaces.Dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class AbstractDto implements Dto, Serializable {
	@Serial
	private static final long serialVersionUID = -3017492350036450891L;

	private String id;
	private String createUser;
	private LocalDateTime createTimestamp;
	private String changeUser;
	private LocalDateTime changeTimestamp;
}

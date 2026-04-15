package com.gyp.authservice.dtos.customerauth;

import java.io.Serial;
import java.time.LocalDateTime;

import com.gyp.authservice.dtos.AbstractDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerResponseDto extends AbstractDto {
	@Serial
	private static final long serialVersionUID = 7341818919457209150L;
	private String id;
	private String name;
	private String email;
	private String phoneNumber;
	private LocalDateTime dob;
	private String provider = "local";
	private String providerId;
}
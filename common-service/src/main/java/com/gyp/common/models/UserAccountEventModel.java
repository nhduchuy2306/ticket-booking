package com.gyp.common.models;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserAccountEventModel {
	private String id;
	private String name;
	private String username;
	private LocalDateTime dob;
	private String phoneNumber;
	private String actions;
}

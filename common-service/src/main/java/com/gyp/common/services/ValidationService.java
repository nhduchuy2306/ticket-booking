package com.gyp.common.services;

import java.util.List;

import com.gyp.common.dtos.ValidationObjectDto;
import com.gyp.common.intefaces.Request;

public interface ValidationService {
	List<ValidationObjectDto> validationRules(Request request);
}

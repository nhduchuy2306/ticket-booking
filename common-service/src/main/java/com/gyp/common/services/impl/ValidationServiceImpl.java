package com.gyp.common.services.impl;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import jakarta.validation.constraints.Size;

import com.gyp.common.dtos.ValidationObjectDto;
import com.gyp.common.intefaces.Request;
import com.gyp.common.services.ValidationService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ValidationServiceImpl implements ValidationService {
	private final ApplicationContext applicationContext;

	@Override
	public List<ValidationObjectDto> validationRules(Request request) {
		List<ValidationObjectDto> rules = new ArrayList<>();
		Class<?> clazz = request.getClass();
		Method[] methods = clazz.getDeclaredMethods();

		for(Method method : methods) {
			Annotation[] annotations = method.getDeclaredAnnotations();

			for(Annotation annotation : annotations) {
				if(annotation instanceof Size size) {
					ValidationObjectDto dto = ValidationObjectDto.builder()
							.type("size")
							.message(size.message())
							.fieldName(method.getName())
							.build();
					rules.add(dto);
				}
			}
		}
		return rules;
	}

	public void listAllRequestImplementations() {
		Map<String, Request> beansOfType = applicationContext.getBeansOfType(Request.class);
		for(Map.Entry<String, Request> entry : beansOfType.entrySet()) {
			System.out.println("Bean name: " + entry.getKey() + ", Class: " + entry.getValue().getClass().getName());
		}
	}
}

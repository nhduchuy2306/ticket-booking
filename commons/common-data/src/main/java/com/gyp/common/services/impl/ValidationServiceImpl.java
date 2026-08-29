package com.gyp.common.services.impl;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import com.gyp.common.services.ValidationService;
import com.gyp.common.validators.criteria.ValidationDetail;
import com.gyp.common.validators.criteria.ValidationInfo;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ValidationServiceImpl implements ValidationService {
	private static final String DEFAULT_NOT_NULL_MESSAGE = "This field cannot be null";
	private static final String DEFAULT_NOT_BLANK_MESSAGE = "This field cannot be blank";
	private static final String DEFAULT_NOT_EMPTY_MESSAGE = "This field cannot be empty";
	private static final String DEFAULT_EMAIL_MESSAGE = "Invalid email format";
	private static final String DEFAULT_PATTERN_MESSAGE = "Invalid format";
	private static final String DEFAULT_DECIMAL_MIN_MESSAGE = "Value must be at least %s";
	private static final String DEFAULT_DECIMAL_MAX_MESSAGE = "Value must be at most %s";
	private static final String DEFAULT_MIN_MESSAGE = "Value must be at least %d";
	private static final String DEFAULT_MAX_MESSAGE = "Value must be at most %d";
	private static final String DEFAULT_LENGTH_MESSAGE = "Length must be between %d and %d";
	private static final String DEFAULT_SIZE_MESSAGE = "Size must be between %d and %d";

	@Override
	public ValidationInfo extractValidationInfo(Class<?> clazz) {
		List<ValidationDetail> validationDetails = new ArrayList<>();

		Field[] fields = clazz.getDeclaredFields();

		for(Field field : fields) {
			Annotation[] annotations = field.getAnnotations();

			for(Annotation annotation : annotations) {
				ValidationDetail detail = processAnnotation(annotation, field.getName());
				if(detail != null) {
					validationDetails.add(detail);
				}
			}
		}
		return ValidationInfo.builder().className(clazz.getSimpleName()).details(validationDetails).build();
	}

	private ValidationDetail processAnnotation(Annotation annotation, String fieldName) {
		String annotationType = getAnnotationName(annotation);
		try {
			return switch(annotationType) {
				case "NotNull" -> processNotNull((NotNull)annotation, fieldName);
				case "NotBlank" -> processNotBlank((NotBlank)annotation, fieldName);
				case "NotEmpty" -> processNotEmpty((NotEmpty)annotation, fieldName);
				case "Length" -> processLength((Length)annotation, fieldName);
				case "Size" -> processSize((Size)annotation, fieldName);
				case "Min" -> processMin((Min)annotation, fieldName);
				case "Max" -> processMax((Max)annotation, fieldName);
				case "Email" -> processEmail((Email)annotation, fieldName);
				case "Pattern" -> processPattern((Pattern)annotation, fieldName);
				case "DecimalMin" -> processDecimalMin((DecimalMin)annotation, fieldName);
				case "DecimalMax" -> processDecimalMax((DecimalMax)annotation, fieldName);
				default -> throw new RuntimeException("Unsupported annotation type: " + annotationType);
			};
		} catch(Exception e) {
			throw new IllegalArgumentException(
					"Error processing annotation " + annotationType + " on field " + fieldName, e);
		}
	}

	private ValidationDetail processNotNull(NotNull annotation, String fieldName) {
		return new ValidationDetail(getAnnotationName(annotation), fieldName, DEFAULT_NOT_NULL_MESSAGE);
	}

	private ValidationDetail processNotBlank(NotBlank annotation, String fieldName) {
		return new ValidationDetail(getAnnotationName(annotation), fieldName, DEFAULT_NOT_BLANK_MESSAGE);
	}

	private ValidationDetail processNotEmpty(NotEmpty annotation, String fieldName) {
		return new ValidationDetail(getAnnotationName(annotation), fieldName, DEFAULT_NOT_EMPTY_MESSAGE);
	}

	private ValidationDetail processLength(Length annotation, String fieldName) {
		String message = String.format(DEFAULT_LENGTH_MESSAGE, annotation.min(), annotation.max());
		ValidationDetail info = new ValidationDetail(getAnnotationName(annotation), fieldName, message);
		info.addAttribute("min", annotation.min());
		info.addAttribute("max", annotation.max());
		return info;
	}

	private ValidationDetail processSize(Size annotation, String fieldName) {
		String message = String.format(DEFAULT_SIZE_MESSAGE, annotation.min(), annotation.max());
		ValidationDetail info = new ValidationDetail(getAnnotationName(annotation), fieldName, message);
		info.addAttribute("min", annotation.min());
		info.addAttribute("max", annotation.max());
		return info;
	}

	private ValidationDetail processMin(Min annotation, String fieldName) {
		String message = String.format(DEFAULT_MIN_MESSAGE, annotation.value());
		ValidationDetail info = new ValidationDetail(getAnnotationName(annotation), fieldName, message);
		info.addAttribute("value", annotation.value());
		return info;
	}

	private ValidationDetail processMax(Max annotation, String fieldName) {
		String message = String.format(DEFAULT_MAX_MESSAGE, annotation.value());
		ValidationDetail info = new ValidationDetail(getAnnotationName(annotation), fieldName, message);
		info.addAttribute("value", annotation.value());
		return info;
	}

	private ValidationDetail processEmail(Email annotation, String fieldName) {
		return new ValidationDetail(getAnnotationName(annotation), fieldName, DEFAULT_EMAIL_MESSAGE);
	}

	private ValidationDetail processPattern(Pattern annotation, String fieldName) {
		ValidationDetail info = new ValidationDetail(getAnnotationName(annotation), fieldName, DEFAULT_PATTERN_MESSAGE);
		info.addAttribute("regexp", annotation.regexp());
		return info;
	}

	private ValidationDetail processDecimalMin(DecimalMin annotation, String fieldName) {
		String message = String.format(DEFAULT_DECIMAL_MIN_MESSAGE, annotation.value());
		ValidationDetail info = new ValidationDetail(getAnnotationName(annotation), fieldName, message);
		info.addAttribute("value", annotation.value());
		info.addAttribute("inclusive", annotation.inclusive());
		return info;
	}

	private ValidationDetail processDecimalMax(DecimalMax annotation, String fieldName) {
		String message = String.format(DEFAULT_DECIMAL_MAX_MESSAGE, annotation.value());
		ValidationDetail info = new ValidationDetail(getAnnotationName(annotation), fieldName, message);
		info.addAttribute("value", annotation.value());
		info.addAttribute("inclusive", annotation.inclusive());
		return info;
	}

	private String getAnnotationName(Annotation annotation) {
		return annotation.annotationType().getSimpleName();
	}
}

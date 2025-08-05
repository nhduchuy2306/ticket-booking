package com.gyp.common.dtos.validation;

import java.util.ArrayList;
import java.util.List;

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
public class ValidationResult {
	@Builder.Default
	private boolean valid = true;

	@Builder.Default
	private List<ValidationError> errors = new ArrayList<>();

	@Builder.Default
	private List<ValidationWarning> warnings = new ArrayList<>();

	// Add error methods
	public void addError(String field, String message) {
		valid = false;
		errors.add(ValidationError.builder()
				.field(field)
				.message(message)
				.build());
	}

	public void addError(String field, String message, String code) {
		valid = false;
		errors.add(ValidationError.builder()
				.field(field)
				.message(message)
				.errorCode(code)
				.build());
	}

	// Add warning methods
	public void addWarning(String field, String message) {
		warnings.add(ValidationWarning.builder()
				.field(field)
				.message(message)
				.build());
	}

	// Check methods
	public boolean hasErrors() {
		return !errors.isEmpty();
	}

	public boolean hasWarnings() {
		return !warnings.isEmpty();
	}

	// Combine multiple validation results
	public ValidationResult combine(ValidationResult other) {
		ValidationResult combined = ValidationResult.builder()
				.valid(valid && other.valid)
				.errors(new ArrayList<>(this.errors))
				.warnings(new ArrayList<>(this.warnings))
				.build();

		combined.errors.addAll(other.errors);
		combined.warnings.addAll(other.warnings);

		return combined;
	}

	// Static factory methods
	public static ValidationResult success() {
		return ValidationResult.builder().valid(true).build();
	}

	public static ValidationResult failure(String field, String message) {
		return ValidationResult.builder()
				.valid(false)
				.errors(List.of(ValidationError.builder()
						.field(field)
						.message(message)
						.build()))
				.build();
	}
}

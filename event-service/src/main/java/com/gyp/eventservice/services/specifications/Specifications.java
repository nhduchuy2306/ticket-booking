package com.gyp.eventservice.services.specifications;

import java.util.Collection;

import org.springframework.data.jpa.domain.Specification;

public final class Specifications {
	private Specifications() {
	}

	public static <T> Specification<T> likeSpecification(String fieldName, String value, MatchMode mode) {
		return (root, query, criteriaBuilder) -> {
			if(value == null || value.trim().isEmpty() || mode == null) {
				return criteriaBuilder.conjunction();
			}

			String pattern = switch(mode) {
				case STARTS_WITH -> value.toLowerCase() + "%";
				case ENDS_WITH -> "%" + value.toLowerCase();
				default -> "%" + value.toLowerCase() + "%";
			};

			return criteriaBuilder.like(criteriaBuilder.lower(root.get(fieldName)), pattern);
		};
	}

	public static <T> Specification<T> equalSpecification(String fieldName, Object value) {
		return (root, query, criteriaBuilder) -> {
			if(value == null || value.toString().trim().isEmpty()) {
				return criteriaBuilder.conjunction();
			}
			return criteriaBuilder.equal(root.get(fieldName), value);
		};
	}

	public static <T, Y extends Comparable<? super Y>> Specification<T> betweenSpecification(String fieldName, Y start,
			Y end) {
		return (root, query, criteriaBuilder) -> {
			if(start != null && end != null) {
				return criteriaBuilder.between(root.get(fieldName), start, end);
			} else if(start != null) {
				return criteriaBuilder.greaterThanOrEqualTo(root.get(fieldName), start);
			} else if(end != null) {
				return criteriaBuilder.lessThanOrEqualTo(root.get(fieldName), end);
			} else {
				return criteriaBuilder.conjunction();
			}
		};
	}

	public static <T, Y extends Comparable<? super Y>> Specification<T> greaterThanOrEqualToSpecification(
			String fieldName, Y value) {
		return (root, query, criteriaBuilder) -> {
			if(value == null) {
				return criteriaBuilder.conjunction();
			}
			return criteriaBuilder.greaterThanOrEqualTo(root.get(fieldName), value);
		};
	}

	public static <T, Y extends Comparable<? super Y>> Specification<T> lessThanOrEqualToSpecification(String fieldName,
			Y value) {
		return (root, query, criteriaBuilder) -> {
			if(value == null) {
				return criteriaBuilder.conjunction();
			}
			return criteriaBuilder.lessThanOrEqualTo(root.get(fieldName), value);
		};
	}

	public static <T> Specification<T> inSpecification(String fieldName, Collection<?> values) {
		return (root, query, criteriaBuilder) -> {
			if(values == null || values.isEmpty()) {
				return criteriaBuilder.conjunction();
			}
			return root.get(fieldName).in(values);
		};
	}

	public static <T> Specification<T> notEqualSpecification(String fieldName, Object value) {
		return (root, query, criteriaBuilder) -> {
			if(value == null || value.toString().trim().isEmpty()) {
				return criteriaBuilder.conjunction();
			}
			return criteriaBuilder.notEqual(root.get(fieldName), value);
		};
	}

	public static <T> Specification<T> isNullSpecification(String fieldName) {
		return (root, query, criteriaBuilder) -> criteriaBuilder.isNull(root.get(fieldName));
	}

	public static <T> Specification<T> isNotNullSpecification(String fieldName) {
		return (root, query, criteriaBuilder) -> criteriaBuilder.isNotNull(root.get(fieldName));
	}

	public static <T> Specification<T> booleanSpecification(String fieldName, Boolean value) {
		return (root, query, criteriaBuilder) -> {
			if(value == null) {
				return criteriaBuilder.conjunction();
			}
			return criteriaBuilder.equal(root.get(fieldName), value);
		};
	}

	public enum MatchMode {
		STARTS_WITH,
		ENDS_WITH,
		CONTAINS
	}
}


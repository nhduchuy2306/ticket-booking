package com.gyp.common.services.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import com.gyp.common.exceptions.ResourceDuplicateException;
import com.gyp.common.services.DataIntegrityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DataIntegrityServiceImpl implements DataIntegrityService {

	@PersistenceContext
	private final EntityManager entityManager;

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, List<Map<String, Object>>> findReferences(String schema, String tableName, String id) {
		// Validate inputs to prevent SQL injection
		if(!isValidIdentifier(schema) || !isValidIdentifier(tableName)) {
			throw new IllegalArgumentException("Invalid schema or table name");
		}

		List<Object[]> results = entityManager.createNativeQuery("""
						    SELECT TABLE_NAME, COLUMN_NAME
						    FROM information_schema.KEY_COLUMN_USAGE
						    WHERE REFERENCED_TABLE_NAME = :tableName
						      AND REFERENCED_COLUMN_NAME = 'id'
						      AND TABLE_SCHEMA = :schema
						""")
				.setParameter("tableName", tableName)
				.setParameter("schema", schema)
				.getResultList();

		Map<String, List<Map<String, Object>>> recordsByTable = new HashMap<>();

		for(var row : results) {
			String table = row[0].toString();
			String column = row[1].toString();

			// Validate identifiers to prevent SQL injection
			if(!isValidIdentifier(table) || !isValidIdentifier(column)) {
				continue; // Skip invalid identifiers
			}

			String sql = String.format("SELECT * FROM %s.%s WHERE %s = :id", schema, table, column);
			List<Object[]> rows = entityManager.createNativeQuery(sql)
					.setParameter("id", id)
					.getResultList();

			// Get column metadata for mapping
			List<String> columnNames = getColumnNames(schema, table);

			// Map results
			List<Map<String, Object>> mappedRows = rows.stream()
					.map(data -> IntStream.range(0, columnNames.size())
							.boxed()
							.collect(Collectors.toMap(
									columnNames::get,
									i -> i < data.length ? data[i] : null,
									(a, b) -> b,
									LinkedHashMap::new)))
					.collect(Collectors.toList());

			recordsByTable.put(table, mappedRows);
		}

		return recordsByTable;
	}

	private boolean isValidIdentifier(String identifier) {
		if(identifier == null || identifier.isEmpty()) {
			return false;
		}
		// Allow alphanumeric characters and underscores, must start with letter or underscore
		return identifier.matches("^[a-zA-Z_][a-zA-Z0-9_]*$");
	}

	@SuppressWarnings("unchecked")
	private List<String> getColumnNames(String schema, String tableName) {
		List<Object> columnResults = entityManager.createNativeQuery("""
						    SELECT COLUMN_NAME
						    FROM information_schema.COLUMNS
						    WHERE TABLE_SCHEMA = :schema
						      AND TABLE_NAME = :tableName
						    ORDER BY ORDINAL_POSITION
						""")
				.setParameter("schema", schema)
				.setParameter("tableName", tableName)
				.getResultList();

		return columnResults.stream()
				.map(Object::toString)
				.collect(Collectors.toList());
	}

	@Override
	public boolean isDuplicate(Class<?> clazz, Map<String, Object> fieldValues, Object excludeId) {
		if(fieldValues == null || fieldValues.isEmpty()) {
			return false;
		}

		try {
			CriteriaBuilder cb = entityManager.getCriteriaBuilder();
			CriteriaQuery<Long> query = cb.createQuery(Long.class);
			Root<?> root = query.from(clazz);

			// Select count
			query.select(cb.count(root));

			// Build predicates
			List<Predicate> predicates = buildFieldPredicates(cb, root, fieldValues);

			// Add exclude condition
			if(excludeId != null) {
				predicates.add(cb.notEqual(root.get("id"), excludeId));
			}

			// Apply predicates
			if(!predicates.isEmpty()) {
				query.where(cb.and(predicates.toArray(new Predicate[0])));
			}

			// Execute and return result
			Long count = entityManager.createQuery(query).getSingleResult();
			return count > 0;
		} catch(ResourceDuplicateException e) {
			throw new ResourceDuplicateException("Error checking for duplicates in " + clazz.getSimpleName(), e);
		}
	}

	private List<Predicate> buildFieldPredicates(CriteriaBuilder cb, Root<?> root, Map<String, Object> fieldValues) {
		List<Predicate> predicates = new ArrayList<>();

		fieldValues.forEach((fieldName, value) -> {
			try {
				if(value != null) {
					predicates.add(cb.equal(root.get(fieldName), value));
				} else {
					predicates.add(cb.isNull(root.get(fieldName)));
				}
			} catch(IllegalArgumentException e) {
				throw new IllegalArgumentException(
						"Invalid field name: " + fieldName + " for entity: " + root.getJavaType().getSimpleName(), e);
			}
		});

		return predicates;
	}
}

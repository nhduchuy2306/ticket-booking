package com.gyp.eventservice.controllers;

import java.util.Optional;

import com.gyp.common.controllers.AbstractValidatableController;
import com.gyp.common.dtos.pagination.PaginatedDto;
import com.gyp.common.exceptions.ResourceNotFoundException;
import com.gyp.common.services.DataIntegrityService;
import com.gyp.eventservice.dtos.category.CategoryRequestDto;
import com.gyp.eventservice.services.CategoryService;
import com.gyp.eventservice.services.criteria.CategorySearchCriteria;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(CategoryController.CATEGORY_CONTROLLER_RESOURCE_PATH)
public class CategoryController extends AbstractValidatableController {
	public static final String CATEGORY_CONTROLLER_RESOURCE_PATH = "/categories";

	private final CategoryService categoryService;
	private final DataIntegrityService dataIntegrityService;

	@GetMapping
	public ResponseEntity<?> getAllCategories(
			@RequestParam(value = "id", required = false) String id,
			@RequestParam(value = "name", required = false) String name,
			@RequestParam(value = "description", required = false) String description,
			@RequestParam(value = "sortBy", required = false) String sortBy,
			@RequestParam(value = "page", required = false) Optional<Integer> page,
			@RequestParam(value = "size", required = false) Optional<Integer> size) {
		String organizationId = getCurrentOrganizationId();
		CategorySearchCriteria criteria = new CategorySearchCriteria(id, name, description, sortBy, organizationId);
		PaginatedDto pagination = PaginatedDto.builder()
				.page(page.orElse(0))
				.size(size.orElse(10))
				.build();
		return ResponseEntity.ok(categoryService.getAllCategories(criteria, pagination));
	}

	@GetMapping("/{" + ID_PARAM + "}")
	public ResponseEntity<?> getCategoryById(@PathVariable(ID_PARAM) String id) {
		if(id == null || id.isEmpty()) {
			return ResponseEntity.badRequest().body("Category ID must not be null or empty");
		}
		try {
			var response = categoryService.getCategoryById(id);
			if(response == null) {
				throw new ResourceNotFoundException("Category with ID " + id + " not found");
			}
			return ResponseEntity.ok(response);
		} catch(ResourceNotFoundException e) {
			throw new RuntimeException(e);
		}
	}

	@PostMapping
	public ResponseEntity<?> createCategory(@RequestBody CategoryRequestDto categoryRequestDto) {
		if(categoryRequestDto == null || categoryRequestDto.getName() == null || categoryRequestDto.getName()
				.isEmpty()) {
			return ResponseEntity.badRequest().body("Category name must not be null or empty");
		}
		var response = categoryService.createCategory(categoryRequestDto);
		return createResponseCreated(response);
	}

	@PutMapping("/{" + ID_PARAM + "}")
	public ResponseEntity<?> updateCategory(@PathVariable("id") String id,
			@RequestBody CategoryRequestDto categoryRequestDto) {
		if(id == null || id.isEmpty()) {
			return ResponseEntity.badRequest().body("Category ID must not be null or empty");
		}
		if(categoryRequestDto == null || categoryRequestDto.getName() == null || categoryRequestDto.getName()
				.isEmpty()) {
			return ResponseEntity.badRequest().body("Category name must not be null or empty");
		}
		var response = categoryService.updateCategory(id, categoryRequestDto);
		return ResponseEntity.ok(response);
	}

	@DeleteMapping("/{" + ID_PARAM + "}")
	public ResponseEntity<?> deleteCategory(@PathVariable("id") String id) {
		if(id == null || id.isEmpty()) {
			return ResponseEntity.badRequest().body("Category ID must not be null or empty");
		}
		categoryService.deleteCategory(id);
		return ResponseEntity.noContent().build();
	}

	@Override
	@GetMapping("/{" + ID_PARAM + "}" + REFERENCES_PATH)
	public ResponseEntity<?> getReferences(@PathVariable("id") String id) {
		if(StringUtils.isEmpty(id)) {
			return ResponseEntity.badRequest().body("Category ID must not be null or empty");
		}
		var references = dataIntegrityService.findReferences(
				"event_service",
				"category",
				id
		);
		return ResponseEntity.ok(references);
	}

	@Override
	@GetMapping(VALIDATION_PATH)
	public ResponseEntity<?> getValidationDetails() {
		return ResponseEntity.ok(categoryService.validate(CategoryRequestDto.class));
	}
}

package com.gyp.common.validators.rulecheck;

import com.gyp.common.services.DataIntegrityService;
import com.gyp.common.validators.criteria.DuplicateCriteria;

public class CheckDuplicate extends BaseValidatorCheck<DuplicateCriteria> {
	private final DataIntegrityService dataIntegrityService;

	public CheckDuplicate(DuplicateCriteria criteria, DataIntegrityService dataIntegrityService) {
		super(criteria);
		this.dataIntegrityService = dataIntegrityService;
	}

	@Override
	public boolean isValid() {
		return !dataIntegrityService.isDuplicate(
				criteria.getClazz(),
				criteria.getFieldValues(),
				criteria.getExcludeId()
		);
	}

	@Override
	public String getErrorMessage() {
		String fields = String.join(", ", criteria.getFieldValues().keySet());
		return String.format("Duplicate %s found for field(s): %s", criteria.getClazz().getSimpleName(), fields);
	}

	@Override
	public String getFieldName() {
		return String.join(",", criteria.getFieldValues().keySet());
	}

	@Override
	public String getErrorCode() {
		return "DUPLICATE_" + criteria.getClazz().getSimpleName().toUpperCase();
	}

	@Override
	public String getErrorDescription() {
		return String.format("A %s with the same %s already exists",
				criteria.getClazz().getSimpleName(),
				String.join(" and ", criteria.getFieldValues().keySet()));
	}
}

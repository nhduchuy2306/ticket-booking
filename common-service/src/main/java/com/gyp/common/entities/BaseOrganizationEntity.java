package com.gyp.common.entities;

import java.io.Serial;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;

@Getter
@Setter
@MappedSuperclass
@FilterDef(name = BaseOrganizationEntity.ORGANIZATION_FILTER_NAME,
		parameters = @ParamDef(name = BaseOrganizationEntity.ORGANIZATION_FILTER_PARAMETER, type = String.class))
@Filter(name = BaseOrganizationEntity.ORGANIZATION_FILTER_NAME,
		condition = BaseOrganizationEntity.ORGANIZATION_FILTER_CONDITION)
public abstract class BaseOrganizationEntity extends AbstractEntity {
	public static final String ORGANIZATION_FILTER_NAME = "organizationFilter";
	public static final String ORGANIZATION_FILTER_PARAMETER = "organizationId";
	public static final String ORGANIZATION_FILTER_CONDITION = "organization_id = :organizationId";

	@Serial
	private static final long serialVersionUID = 5184674465187044524L;

	@Column(name = "organization_id")
	private String organizationId;
}




package com.gyp.authservice.entities;

import jakarta.persistence.MappedSuperclass;

import com.gyp.common.entities.BaseOrganizationEntity;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@MappedSuperclass
public abstract class OrganizationScopedEntity extends BaseOrganizationEntity {
}



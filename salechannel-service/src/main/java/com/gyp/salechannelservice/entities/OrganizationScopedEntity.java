package com.gyp.salechannelservice.entities;

import jakarta.persistence.MappedSuperclass;

import com.gyp.common.entities.BaseOrganizationEntity;

@MappedSuperclass
public abstract class OrganizationScopedEntity extends BaseOrganizationEntity {
}


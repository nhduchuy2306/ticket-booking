---
name: create-crud-api
description: >-
  Use this skill when the user asks to create a new CRUD API endpoint, entity,
  DTO, mapper, service, repository, or controller within an existing service module.
  Covers the complete vertical slice from entity to REST endpoint.
---

# Create CRUD API (Entity → Controller)

Follow this order to create a complete CRUD vertical slice. Replace `{Resource}` with the entity name (e.g., `Ticket`, `Venue`).

## 1. Entity

```java
package com.gyp.{servicename}.entities;

import java.io.Serial;
import jakarta.persistence.*;
import com.gyp.common.entities.AbstractEntity;
import lombok.*;

@Getter
@Setter
@Table(name = "{RESOURCE_TABLE}")
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class {Resource}Entity extends AbstractEntity {
    @Serial
    private static final long serialVersionUID = 1L; // Generate unique value

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", length = 36)
    private String id;

    // Add fields here. IMPORTANT:
    // - Collection fields with initializers MUST use @Builder.Default
    // Example:
    // @Builder.Default
    // @OneToMany(mappedBy = "parentEntity", cascade = CascadeType.ALL)
    // private List<ChildEntity> children = new ArrayList<>();

    @Column(name = "organization_id", length = 36)
    private String organizationId;
}
```

**Key rules:**
- Always extend `AbstractEntity` (provides createUser, changeUser, createTimestamp, changeTimestamp)
- Use `@Builder` (NOT `@SuperBuilder`)
- Any `List<> field = new ArrayList<>()` or field with default value → add `@Builder.Default`
- Use `String id` with `GenerationType.UUID`

## 2. Request DTO

```java
package com.gyp.{servicename}.dtos.{resource};

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class {Resource}RequestDto {
    @NotBlank(message = "Name is required")
    private String name;
    // Add validated fields
}
```

## 3. Response DTO

```java
package com.gyp.{servicename}.dtos.{resource};

import com.gyp.{servicename}.dtos.AbstractDto;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class {Resource}ResponseDto extends AbstractDto {
    private String id;
    private String name;
    // Mirror entity fields for API response
    // AbstractDto provides: createUser, changeUser, createTimestamp, changeTimestamp
}
```

## 4. Mapper

```java
package com.gyp.{servicename}.mappers;

import java.util.List;
import com.gyp.common.mappers.AbstractMapper;
import com.gyp.{servicename}.dtos.{resource}.{Resource}RequestDto;
import com.gyp.{servicename}.dtos.{resource}.{Resource}ResponseDto;
import com.gyp.{servicename}.entities.{Resource}Entity;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
        mappingInheritanceStrategy = MappingInheritanceStrategy.AUTO_INHERIT_FROM_CONFIG)
public interface {Resource}Mapper extends AbstractMapper {

    {Resource}ResponseDto toResponseDto({Resource}Entity entity);

    List<{Resource}ResponseDto> toResponseDtoList(List<{Resource}Entity> entities);

    @Mapping(target = "id", ignore = true)
    {Resource}Entity toEntity({Resource}RequestDto dto);

    @Mapping(target = "id", ignore = true)
    void updateEntityFromDto({Resource}RequestDto dto, @MappingTarget {Resource}Entity entity);

    @AfterMapping
    default void afterMappingEntity(@MappingTarget {Resource}Entity entity) {
        mapAbstractFieldsToEntity(entity);
    }

    @AfterMapping
    default void afterMappingDto(@MappingTarget {Resource}ResponseDto dto, {Resource}Entity entity) {
        mapAbstractFields(entity, dto);
    }
}
```

**Key rules:**
- ALWAYS include `unmappedTargetPolicy = ReportingPolicy.IGNORE`
- NEVER add `@Mapping(target = "createUser/changeUser/createTimestamp/changeTimestamp", ignore = true)`
- Use `@AfterMapping` with `mapAbstractFieldsToEntity()` and `mapAbstractFields()` from `AbstractMapper`
- Extend `AbstractMapper` from common-service

## 5. Repository

```java
package com.gyp.{servicename}.repositories;

import com.gyp.{servicename}.entities.{Resource}Entity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface {Resource}Repository extends JpaRepository<{Resource}Entity, String>,
        JpaSpecificationExecutor<{Resource}Entity> {
    List<{Resource}Entity> findByOrganizationId(String organizationId);
}
```

## 6. Service

```java
package com.gyp.{servicename}.services;

import java.util.List;
import com.gyp.{servicename}.dtos.{resource}.*;
import com.gyp.{servicename}.entities.{Resource}Entity;
import com.gyp.{servicename}.mappers.{Resource}Mapper;
import com.gyp.{servicename}.repositories.{Resource}Repository;
import com.gyp.common.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class {Resource}Service {
    private final {Resource}Repository repository;
    private final {Resource}Mapper mapper;

    public List<{Resource}ResponseDto> getAll() {
        String orgId = SecurityUtils.getCurrentOrganizationId();
        return mapper.toResponseDtoList(repository.findByOrganizationId(orgId));
    }

    public {Resource}ResponseDto getById(String id) {
        {Resource}Entity entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("{Resource} not found: " + id));
        return mapper.toResponseDto(entity);
    }

    public {Resource}ResponseDto create({Resource}RequestDto dto) {
        {Resource}Entity entity = mapper.toEntity(dto);
        entity.setOrganizationId(SecurityUtils.getCurrentOrganizationId());
        return mapper.toResponseDto(repository.save(entity));
    }

    public {Resource}ResponseDto update(String id, {Resource}RequestDto dto) {
        {Resource}Entity entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("{Resource} not found: " + id));
        mapper.updateEntityFromDto(dto, entity);
        return mapper.toResponseDto(repository.save(entity));
    }

    public void delete(String id) {
        repository.deleteById(id);
    }
}
```

## 7. Controller

```java
package com.gyp.{servicename}.controllers;

import jakarta.validation.Valid;
import com.gyp.common.controllers.AbstractController;
import com.gyp.{servicename}.dtos.{resource}.{Resource}RequestDto;
import com.gyp.{servicename}.services.{Resource}Service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping({Resource}Controller.RESOURCE_PATH)
public class {Resource}Controller extends AbstractController {
    public static final String RESOURCE_PATH = "/{resources}";

    private final {Resource}Service service;

    @GetMapping
    @PreAuthorize("@permissionEvaluator.hasPermission(authentication, #AppPerm.{RESOURCE}, #ActionPerm.READ)")
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/{" + ID_PARAM + "}")
    @PreAuthorize("@permissionEvaluator.hasPermission(authentication, #AppPerm.{RESOURCE}, #ActionPerm.READ)")
    public ResponseEntity<?> getById(@PathVariable(ID_PARAM) String id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @PostMapping
    @PreAuthorize("@permissionEvaluator.hasPermission(authentication, #AppPerm.{RESOURCE}, #ActionPerm.CREATE)")
    public ResponseEntity<?> create(@RequestBody @Valid {Resource}RequestDto dto) {
        return ResponseEntity.ok(service.create(dto));
    }

    @PutMapping("/{" + ID_PARAM + "}")
    @PreAuthorize("@permissionEvaluator.hasPermission(authentication, #AppPerm.{RESOURCE}, #ActionPerm.UPDATE)")
    public ResponseEntity<?> update(@PathVariable(ID_PARAM) String id,
            @RequestBody @Valid {Resource}RequestDto dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @DeleteMapping("/{" + ID_PARAM + "}")
    @PreAuthorize("@permissionEvaluator.hasPermission(authentication, #AppPerm.{RESOURCE}, #ActionPerm.DELETE)")
    public ResponseEntity<?> delete(@PathVariable(ID_PARAM) String id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
```

**Key rules:**
- Extend `AbstractController` (provides `ID_PARAM`, `getCurrentOrganizationId()`, response helpers)
- Define `RESOURCE_PATH` as static final constant
- Use `@PreAuthorize` with permission evaluator for all secured endpoints
- Use `@Valid` on request body parameters

## 8. Add Permission Enum (if new resource type)

In `common-service`, add the new resource to `ApplicationPermission` enum if not already present.

## Verification

Build the specific service:
```bash
.\gradlew.bat :{service-name}:build -x test
```

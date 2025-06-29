package com.gyp.eventservice.repositories;

import java.util.Optional;

import com.gyp.eventservice.entities.CategoryEntity;
import com.gyp.eventservice.repositories.dsl.CategoryRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<CategoryEntity, String>,
		JpaSpecificationExecutor<CategoryEntity>, CategoryRepositoryCustom {
	Optional<CategoryEntity> findByName(String name);
}

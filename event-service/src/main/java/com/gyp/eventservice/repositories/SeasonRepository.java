package com.gyp.eventservice.repositories;

import java.util.Optional;

import com.gyp.common.enums.event.SeasonStatus;
import com.gyp.eventservice.entities.SeasonEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface SeasonRepository extends JpaRepository<SeasonEntity, String>, JpaSpecificationExecutor<SeasonEntity> {
	Optional<SeasonEntity> findByStatus(SeasonStatus status);
}

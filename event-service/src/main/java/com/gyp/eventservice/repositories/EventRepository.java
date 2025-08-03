package com.gyp.eventservice.repositories;

import java.util.List;

import com.gyp.eventservice.entities.EventEntity;
import com.gyp.eventservice.entities.SeasonEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface EventRepository extends JpaRepository<EventEntity, String>, JpaSpecificationExecutor<EventEntity> {

	@Query("""
			SELECT e FROM EventEntity e
			WHERE e.status NOT IN ('DRAFT', 'CANCELLED', 'POSTPONED', 'COMPLETED')
			AND e.organizationId = :organizationId
			ORDER BY e.time.startTime ASC
			""")
	List<EventEntity> findAllActiveEvents(String organizationId);
}

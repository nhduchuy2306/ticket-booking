package com.gyp.eventservice.repositories;

import java.util.List;
import java.util.Optional;

import com.gyp.eventservice.entities.TicketTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TicketTypeRepository
		extends JpaRepository<TicketTypeEntity, String>, JpaSpecificationExecutor<TicketTypeEntity> {
	Optional<TicketTypeEntity> findByName(String ticketTypeName);

	@Query("""
			SELECT DISTINCT t FROM TicketTypeEntity t
			JOIN t.eventSectionMappingEntityList esm
			WHERE esm.eventEntity.id = :eventId
			""")
	List<TicketTypeEntity> findAllByEventEntityId(@Param("eventId") String eventId);

	List<TicketTypeEntity> findAllByIdIn(List<String> ids);
}

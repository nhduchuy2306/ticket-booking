package com.gyp.eventservice.repositories;

import java.util.List;
import java.util.Optional;

import com.gyp.eventservice.entities.TicketTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TicketTypeRepository extends JpaRepository<TicketTypeEntity, String> {
	Optional<TicketTypeEntity> findByName(String ticketTypeName);
	List<TicketTypeEntity> findAllByEventEntityId(String eventId);
}

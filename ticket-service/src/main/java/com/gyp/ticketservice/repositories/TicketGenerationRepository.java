package com.gyp.ticketservice.repositories;

import java.util.Optional;

import com.gyp.ticketservice.entities.TicketGenerationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TicketGenerationRepository extends JpaRepository<TicketGenerationEntity, String> {
	Optional<TicketGenerationEntity> findByTicketNumber(String ticketNumber);
}

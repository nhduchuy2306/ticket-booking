package com.gyp.ticketservice.repositories;

import java.util.List;

import com.gyp.ticketservice.entities.TicketEntity;
import com.gyp.ticketservice.repositories.dsl.TicketRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface TicketRepository extends JpaRepository<TicketEntity, String>, TicketRepositoryCustom,
		JpaSpecificationExecutor<TicketEntity> {
	List<TicketEntity> findAllByEventId(String eventId);
}

package com.gyp.ticket.eventservice.repositories;

import com.gyp.ticket.eventservice.entities.TicketTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TicketTypeRepository extends JpaRepository<TicketTypeEntity, String> {
}

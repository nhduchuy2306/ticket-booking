package com.gyp.ticket.ticketservice.repositories;

import com.gyp.ticket.ticketservice.entities.TicketEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TicketRepository extends JpaRepository<TicketEntity, String> {
}

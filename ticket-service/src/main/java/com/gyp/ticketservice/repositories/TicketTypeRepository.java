package com.gyp.ticketservice.repositories;

import com.gyp.ticketservice.entities.TicketTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TicketTypeRepository extends JpaRepository<TicketTypeEntity, String> {
}

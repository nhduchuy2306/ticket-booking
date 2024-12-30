package com.gyp.ticket.eventservice.repositories;

import com.gyp.ticket.eventservice.entities.VenueEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VenusRepository extends JpaRepository<VenueEntity, String> {
}

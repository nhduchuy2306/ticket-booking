package com.gyp.ticket.eventservice.repositories;

import java.util.Optional;

import com.gyp.ticket.eventservice.entities.SeatMapEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SeatMapRepository extends JpaRepository<SeatMapEntity, String> {
	Optional<SeatMapEntity> findByEventEntityId(String eventId);
}

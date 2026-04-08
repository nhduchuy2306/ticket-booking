package com.gyp.eventservice.repositories;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import com.gyp.eventservice.entities.SeatEntity;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

@Repository
public interface SeatRepository extends JpaRepository<SeatEntity, String> {
	boolean existsByEventId(String eventId);

	Optional<SeatEntity> findByEventIdAndSeatKey(String eventId, String seatKey);

	@Lock(LockModeType.PESSIMISTIC_WRITE)
	List<SeatEntity> findByEventIdAndSeatKeyIn(String eventId, Collection<String> seatKeys);
}
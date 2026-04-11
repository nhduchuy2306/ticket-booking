package com.gyp.eventservice.repositories;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import com.gyp.eventservice.entities.SeatInventoryEntity;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

@Repository
public interface SeatInventoryRepository extends JpaRepository<SeatInventoryEntity, String> {
	boolean existsByEventId(String eventId);

	List<SeatInventoryEntity> findByEventId(String eventId);

	Optional<SeatInventoryEntity> findByEventIdAndSeatKey(String eventId, String seatKey);

	@Lock(LockModeType.PESSIMISTIC_WRITE)
	List<SeatInventoryEntity> findByEventIdAndSeatKeyIn(String eventId, Collection<String> seatKeys);
}
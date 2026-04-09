package com.gyp.eventservice.repositories;

import java.util.Collection;
import java.time.LocalDateTime;
import java.util.List;

import com.gyp.eventservice.entities.SeatHoldEntity;
import com.gyp.eventservice.entities.SeatHoldStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SeatHoldRepository extends JpaRepository<SeatHoldEntity, String> {
	List<SeatHoldEntity> findByEventId(String eventId);

	List<SeatHoldEntity> findByEventIdAndHoldToken(String eventId, String holdToken);

	List<SeatHoldEntity> findByEventIdAndHoldTokenAndStatus(String eventId, String holdToken, SeatHoldStatus status);

	List<SeatHoldEntity> findByEventIdAndHoldTokenAndStatusIn(String eventId, String holdToken,
			Collection<SeatHoldStatus> statuses);

	List<SeatHoldEntity> findByEventIdAndStatusIn(String eventId, Collection<SeatHoldStatus> statuses);

	List<SeatHoldEntity> findByStatusAndExpiresAtBefore(SeatHoldStatus status, LocalDateTime cutoff);
}
package com.gyp.ticketservice.repositories;

import java.util.List;
import java.util.Optional;

import com.gyp.common.enums.event.TicketStatus;
import com.gyp.ticketservice.entities.TicketEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TicketRepository extends JpaRepository<TicketEntity, String>, JpaSpecificationExecutor<TicketEntity> {
	List<TicketEntity> findAllByEventId(String eventId);

	@Modifying
	@Query("""
			UPDATE TicketEntity t
			SET t.status = :status
			WHERE t.eventId = :eventId
			""")
	int updateTicketStatusByEventId(@Param("eventId") String eventId, @Param("status") TicketStatus status);

	Optional<TicketEntity> findBySeatId(String seatId);
}

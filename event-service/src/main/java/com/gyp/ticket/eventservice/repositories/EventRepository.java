package com.gyp.ticket.eventservice.repositories;

import com.gyp.ticket.eventservice.entities.EventEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventRepository extends JpaRepository<EventEntity, String> {
}

package com.gyp.ticket.eventservice.repositories;

import com.gyp.ticket.eventservice.entities.OrganizerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrganizerRepository extends JpaRepository<OrganizerEntity, String> {
}

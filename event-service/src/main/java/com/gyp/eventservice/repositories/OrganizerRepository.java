package com.gyp.eventservice.repositories;

import com.gyp.eventservice.entities.OrganizerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrganizerRepository extends JpaRepository<OrganizerEntity, String> {
}

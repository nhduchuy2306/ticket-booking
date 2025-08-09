package com.gyp.eventservice.repositories;

import com.gyp.eventservice.entities.EventImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventImageRepository extends JpaRepository<EventImageEntity, String> {
}

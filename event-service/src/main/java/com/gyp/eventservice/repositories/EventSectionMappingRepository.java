package com.gyp.eventservice.repositories;

import java.util.List;

import com.gyp.eventservice.entities.EventSectionMappingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventSectionMappingRepository extends JpaRepository<EventSectionMappingEntity, String> {
	List<EventSectionMappingEntity> findAllByEventEntityId(String eventId);
}


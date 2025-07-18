package com.gyp.eventservice.repositories;

import com.gyp.eventservice.entities.VenueMapEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VenueMapRepository extends JpaRepository<VenueMapEntity, String> {
}

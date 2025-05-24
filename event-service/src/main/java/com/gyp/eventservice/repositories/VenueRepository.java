package com.gyp.eventservice.repositories;

import com.gyp.eventservice.entities.VenueEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VenueRepository extends JpaRepository<VenueEntity, String> {
}

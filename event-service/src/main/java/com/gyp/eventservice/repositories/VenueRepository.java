package com.gyp.eventservice.repositories;

import java.util.Optional;

import com.gyp.eventservice.entities.VenueEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VenueRepository extends JpaRepository<VenueEntity, String> {
	Optional<VenueEntity> findByName(String name);

	Optional<VenueEntity> findByLongitudeAndLatitude(double longitude, double latitude);

	Optional<VenueEntity> findByAddress(String address);
}

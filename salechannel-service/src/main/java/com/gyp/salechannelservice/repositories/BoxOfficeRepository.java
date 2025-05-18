package com.gyp.salechannelservice.repositories;

import java.util.List;
import java.util.Optional;

import com.gyp.salechannelservice.entities.BoxOfficesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BoxOfficeRepository extends JpaRepository<BoxOfficesEntity, String> {
	@Query("SELECT bo FROM BoxOfficesEntity bo JOIN bo.saleChannelEntity sc WHERE sc.id = :channelId")
	Optional<BoxOfficesEntity> findBySaleChannelId(@Param("channelId") String channelId);

	List<BoxOfficesEntity> findByLocationNameContaining(String locationName);
}

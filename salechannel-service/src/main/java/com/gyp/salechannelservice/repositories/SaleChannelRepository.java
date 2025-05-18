package com.gyp.salechannelservice.repositories;

import java.util.List;
import java.util.Optional;

import com.gyp.common.enums.salechannel.SaleChannelType;
import com.gyp.salechannelservice.entities.SaleChannelEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SaleChannelRepository extends JpaRepository<SaleChannelEntity, String> {
	List<SaleChannelEntity> findByIsActive(boolean active);

	List<SaleChannelEntity> findByChannelType(SaleChannelType type);

	@Query("SELECT sc FROM SaleChannelEntity sc JOIN sc.eventInfoEntityList e WHERE e = :eventId")
	List<SaleChannelEntity> findAllByEventId(@Param("eventId") String eventId);

	Optional<SaleChannelEntity> findByChannelName(String name);
}

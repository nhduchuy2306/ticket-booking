package com.gyp.salechannelservice.repositories;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.gyp.salechannelservice.entities.ChannelPricingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ChannelPricingRepository extends JpaRepository<ChannelPricingEntity, String> {
	List<ChannelPricingEntity> findByChannelId(String channelId);

	List<ChannelPricingEntity> findByTicketTypeId(String ticketTypeId);

	Optional<ChannelPricingEntity> findByChannelIdAndTicketTypeIdAndIsActiveTrue(String channelId, String ticketTypeId);

	@Query("""
			SELECT cp FROM ChannelPricingEntity cp
			WHERE cp.channelId = :channelId
			AND cp.ticketTypeId = :ticketTypeId
			AND cp.isActive = true
			AND cp.validFrom <= :currentDate
			AND (cp.validUntil IS NULL OR cp.validUntil >= :currentDate)
			""")
	Optional<ChannelPricingEntity> findActiveChannelPricing(@Param("channelId") String channelId,
			@Param("ticketTypeId") String ticketTypeId, @Param("currentDate") LocalDateTime currentDate);
}

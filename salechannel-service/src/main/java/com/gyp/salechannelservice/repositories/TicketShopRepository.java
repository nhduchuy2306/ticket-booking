package com.gyp.salechannelservice.repositories;

import java.util.List;
import java.util.Optional;

import com.gyp.salechannelservice.entities.TicketShopEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TicketShopRepository extends JpaRepository<TicketShopEntity, String> {
	Optional<TicketShopEntity> findBySubdomain(String subdomain);

	Optional<TicketShopEntity> findByCustomDomain(String customDomain);

	List<TicketShopEntity> findByOrganizerId(String organizerId);

	@Query("SELECT ts FROM TicketShopEntity ts JOIN ts.saleChannelEntity sc WHERE sc.id = :channelId")
	Optional<TicketShopEntity> findBySaleChannelId(@Param("channelId") String channelId);

	boolean existsBySubdomain(String subdomain);

	boolean existsByCustomDomain(String customDomain);
}

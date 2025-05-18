package com.gyp.salechannelservice.repositories;

import java.time.LocalDateTime;
import java.util.List;

import com.gyp.salechannelservice.entities.ChannelSalesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ChannelSalesRepository extends JpaRepository<ChannelSalesEntity, String> {
	List<ChannelSalesEntity> findByChannelId(String channelId);

	List<ChannelSalesEntity> findByEventId(String eventId);

	List<ChannelSalesEntity> findByChannelIdAndEventId(String channelId, String eventId);

	@Query("SELECT cs FROM ChannelSalesEntity cs WHERE cs.saleDate BETWEEN :startDate AND :endDate")
	List<ChannelSalesEntity> findBySaleDateBetween(
			@Param("startDate") LocalDateTime startDate,
			@Param("endDate") LocalDateTime endDate);

	@Query("SELECT SUM(cs.totalRevenue) FROM ChannelSalesEntity cs WHERE cs.channelId = :channelId")
	Double sumTotalRevenueByChannelId(@Param("channelId") String channelId);

	@Query("SELECT SUM(cs.totalTicketsSold) FROM ChannelSalesEntity cs WHERE cs.eventId = :eventId")
	Integer sumTotalTicketsSoldByEventId(@Param("eventId") String eventId);
}

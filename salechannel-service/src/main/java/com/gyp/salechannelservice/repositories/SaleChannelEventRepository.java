package com.gyp.salechannelservice.repositories;

import java.util.List;
import java.util.Optional;

import com.gyp.salechannelservice.entities.SaleChannelEventEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SaleChannelEventRepository extends JpaRepository<SaleChannelEventEntity, String> {
	Optional<SaleChannelEventEntity> findByEventIdAndSaleChannelEntity_Id(String eventId, String saleChannelId);

	List<SaleChannelEventEntity> findAllByEventId(String eventId);
}

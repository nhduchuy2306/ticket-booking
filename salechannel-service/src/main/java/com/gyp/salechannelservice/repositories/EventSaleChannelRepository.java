package com.gyp.salechannelservice.repositories;

import com.gyp.salechannelservice.entities.EventSaleChannelEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventSaleChannelRepository extends JpaRepository<EventSaleChannelEntity, String> {
}

package com.gyp.salechannelservice.repositories;

import com.gyp.salechannelservice.entities.SaleChannelOrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SaleChannelOrderRepository extends JpaRepository<SaleChannelOrderEntity, String> {
}

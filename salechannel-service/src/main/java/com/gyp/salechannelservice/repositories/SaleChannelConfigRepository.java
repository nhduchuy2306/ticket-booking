package com.gyp.salechannelservice.repositories;

import com.gyp.salechannelservice.entities.SaleChannelConfigEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SaleChannelConfigRepository extends JpaRepository<SaleChannelConfigEntity, String> {
}

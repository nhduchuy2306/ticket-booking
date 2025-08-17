package com.gyp.salechannelservice.repositories;

import java.util.List;

import com.gyp.common.enums.salechannel.SaleChannelStatus;
import com.gyp.common.enums.salechannel.SaleChannelType;
import com.gyp.salechannelservice.entities.SaleChannelEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SaleChannelRepository extends JpaRepository<SaleChannelEntity, String> {
	List<SaleChannelEntity> findByStatus(SaleChannelStatus status);

	List<SaleChannelEntity> findByType(SaleChannelType type);
}

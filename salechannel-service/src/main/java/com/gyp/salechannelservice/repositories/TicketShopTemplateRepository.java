package com.gyp.salechannelservice.repositories;

import java.util.List;

import com.gyp.salechannelservice.entities.TicketShopTemplateEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TicketShopTemplateRepository extends JpaRepository<TicketShopTemplateEntity, String> {
	List<TicketShopTemplateEntity> findByDefaultTrue();

	List<TicketShopTemplateEntity> findByNameContainingIgnoreCase(String name);
}

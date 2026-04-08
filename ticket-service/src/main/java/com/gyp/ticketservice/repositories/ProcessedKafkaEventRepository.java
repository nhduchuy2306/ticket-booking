package com.gyp.ticketservice.repositories;

import com.gyp.ticketservice.entities.ProcessedKafkaEventEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProcessedKafkaEventRepository extends JpaRepository<ProcessedKafkaEventEntity, String> {
	boolean existsByConsumerNameAndEventKey(String consumerName, String eventKey);
}
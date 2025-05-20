package com.gyp.salechannelservice.repositories;

import com.gyp.salechannelservice.entities.EventInfoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventInfoRepository extends JpaRepository<EventInfoEntity, String> {
}

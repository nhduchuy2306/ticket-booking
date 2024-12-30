package com.gyp.ticket.authservice.repositories;

import com.gyp.ticket.authservice.entities.UserGroupEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserGroupRepository extends JpaRepository<UserGroupEntity, String> {
}

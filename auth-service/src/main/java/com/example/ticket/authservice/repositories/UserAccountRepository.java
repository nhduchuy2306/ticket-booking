package com.example.ticket.authservice.repositories;

import java.util.Optional;

import com.example.ticket.authservice.entities.UserAccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserAccountRepository extends JpaRepository<UserAccountEntity, String> {
	Optional<UserAccountEntity> findByName(String name);

	@Query("SELECT u FROM UserAccountEntity u JOIN FETCH u.userGroupEntityList WHERE u.username = :username")
	Optional<UserAccountEntity> findByUsername(String username);
}

package com.gyp.authservice.repositories;

import java.time.LocalDateTime;
import java.util.Optional;

import com.gyp.authservice.entities.CustomerRefreshTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRefreshTokenRepository extends JpaRepository<CustomerRefreshTokenEntity, String> {
	Optional<CustomerRefreshTokenEntity> findByTokenAndRevokedFalseAndExpiryAfter(String token,
			LocalDateTime currentTime);
}
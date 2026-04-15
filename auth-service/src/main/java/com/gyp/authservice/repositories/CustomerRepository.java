package com.gyp.authservice.repositories;

import java.util.Optional;

import com.gyp.authservice.entities.CustomerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<CustomerEntity, String> {
	Optional<CustomerEntity> findByEmailAndProvider(String email, String provider);

	Optional<CustomerEntity> findByProviderAndProviderId(String provider, String providerId);

	Optional<CustomerEntity> findByEmail(String email);

	boolean existsByEmail(String email);
}
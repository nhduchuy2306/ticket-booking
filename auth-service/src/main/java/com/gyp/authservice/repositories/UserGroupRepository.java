package com.gyp.authservice.repositories;

import java.util.List;
import java.util.Optional;

import com.gyp.authservice.entities.UserAccountEntity;
import com.gyp.authservice.entities.UserGroupEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface UserGroupRepository extends JpaRepository<UserGroupEntity, String>,
		JpaSpecificationExecutor<UserGroupEntity> {
	Optional<UserGroupEntity> findByIdAndOrganizationEntityId(String id, String organizationEntity);

	List<UserGroupEntity> findByUserAccountEntityList(List<UserAccountEntity> userAccountEntityList);
}

package com.gyp.authservice.repositories;

import java.util.List;
import java.util.Optional;

import com.gyp.authservice.entities.UserAccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserAccountRepository extends JpaRepository<UserAccountEntity, String>,
		JpaSpecificationExecutor<UserAccountEntity> {
	Optional<UserAccountEntity> findByName(String name);

	Optional<UserAccountEntity> findByEmailAndName(String email, String name);

	Boolean existsByEmail(String email);

	@Query("SELECT u FROM UserAccountEntity u JOIN FETCH u.userGroupEntityList WHERE u.username = :username")
	Optional<UserAccountEntity> findByUsername(String username);

	@Query(value = """
			SELECT DISTINCT ua.*
			FROM useraccount ua
			JOIN userpermissions up ON ua.id = up.user_account_id
			JOIN usergroup ug ON ug.id = up.user_group_id
			JOIN JSON_TABLE(
			    ug.user_group_permissions -> '$.permissionItems',
			    '$[*]' COLUMNS (
			        applicationId VARCHAR(255) PATH '$.applicationId',
			        actions JSON PATH '$.actions'
			    )
			) AS perms ON TRUE
			WHERE perms.applicationId = 'app.event'
			AND JSON_CONTAINS(perms.actions, '["CREATE"]') IS NOT NULL
			AND JSON_CONTAINS(perms.actions, '["READ"]') IS NOT NULL
			AND JSON_CONTAINS(perms.actions, '["UPDATE"]') IS NOT NULL
			AND JSON_CONTAINS(perms.actions, '["DELETE"]') IS NOT NULL;
			""", nativeQuery = true)
	List<UserAccountEntity> findAllWithAppEventCrudPermissions();

	@Query("""
				SELECT u FROM UserAccountEntity u
				JOIN FETCH u.userGroupEntityList ug
				WHERE ug.id = :userGroupId
				AND u.id = :userAccountId
			""")
	Optional<UserAccountEntity> findByUserGroupId(String userGroupId, String userAccountId);
}

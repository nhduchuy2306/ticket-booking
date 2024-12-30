package com.gyp.ticket.authservice.repositories;

import java.util.List;
import java.util.Optional;

import com.gyp.ticket.authservice.entities.UserAccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserAccountRepository extends JpaRepository<UserAccountEntity, String> {
	Optional<UserAccountEntity> findByName(String name);

	@Query("SELECT u FROM UserAccountEntity u JOIN FETCH u.userGroupEntityList WHERE u.username = :username")
	Optional<UserAccountEntity> findByUsername(String username);

	@Query(value = """
			SELECT DISTINCT ua.*
			FROM useraccount ua
			JOIN userpermissions up ON ua.id = up.user_account_id
			JOIN usergroup ug ON ug.id = up.user_group_id
			JOIN JSON_TABLE(
			    ug.user_group_permissions -> '$.usergrouppermissions.permissionItems',
			    '$[*]' COLUMNS (
			        applicationId VARCHAR(255) PATH '$.permissionitem.applicationId',
			        actions JSON PATH '$.permissionitem.actions'
			    )
			) AS perms ON TRUE
			WHERE perms.applicationId = 'app.event'
			AND JSON_CONTAINS(perms.actions, '["CREATE"]') IS NOT NULL
			AND JSON_CONTAINS(perms.actions, '["READ"]') IS NOT NULL
			AND JSON_CONTAINS(perms.actions, '["UPDATE"]') IS NOT NULL
			AND JSON_CONTAINS(perms.actions, '["DELETE"]') IS NOT NULL;
			""", nativeQuery = true)
	List<UserAccountEntity> findAllWithAppEventCrudPermissions();

}

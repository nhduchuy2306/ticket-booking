package com.gyp.authservice.upgraders;

import com.gyp.authservice.entities.UserGroupEntity;
import com.gyp.authservice.services.UserGroupService;
import com.gyp.common.enums.version.Version;
import com.gyp.common.upgrader.BaseUpgrader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserGroupUpgrader extends BaseUpgrader {
	private final UserGroupService userGroupService;

	@Override
	protected void doMigrate() {
		var userGroupList = userGroupService.getListUserGroups();
		if(!CollectionUtils.isEmpty(userGroupList)) {
			log.info("OK");
		}
	}

	@Override
	public Version getVersion() {
		return Version.V1_0_0;
	}

	@Override
	public Class<?> getClazz() {
		return UserGroupEntity.class;
	}
}

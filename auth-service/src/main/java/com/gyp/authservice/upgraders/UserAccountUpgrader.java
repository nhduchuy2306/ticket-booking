package com.gyp.authservice.upgraders;

import jakarta.transaction.Transactional;

import com.gyp.authservice.entities.UserAccountEntity;
import com.gyp.authservice.services.UserAccountService;
import com.gyp.common.enums.version.Version;
import com.gyp.common.upgrader.BaseUpgrader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

@Slf4j
@Component
@Transactional
@RequiredArgsConstructor
public class UserAccountUpgrader extends BaseUpgrader {
	private final UserAccountService userAccountService;

	@Override
	protected void doMigrate() {
		var userAccountList = userAccountService.getUserAccountList();
		if(!CollectionUtils.isEmpty(userAccountList)) {
			log.info("OK");
		}
	}

	@Override
	public Version getVersion() {
		return Version.V1_0_0;
	}

	@Override
	public Class<?> getClazz() {
		return UserAccountEntity.class;
	}
}

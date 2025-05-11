package com.gyp.common.upgrader;

import com.gyp.common.exceptions.DataUpgraderException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class BaseUpgrader implements DataUpgrader {
	@Override
	public void migrate() throws DataUpgraderException {
		try {
			log.info("Running upgrader {}: {}", getVersion(), getDescription());
			doMigrate();
			log.info("Successfully upgrader");
		} catch(Exception e) {
			throw new DataUpgraderException("Migration failed: " + getVersion(), e);
		}
	}

	@Override
	public String getDescription() {
		return "This is upgrader for " + getClazz().getSimpleName();
	}

	protected abstract void doMigrate();
}

package com.gyp.common.upgrader;

import com.gyp.common.enums.version.Version;
import com.gyp.common.exceptions.DataUpgraderException;

public interface DataUpgrader {
	Version getVersion();

	String getDescription();

	Class<?> getClazz();

	void migrate() throws DataUpgraderException;
}

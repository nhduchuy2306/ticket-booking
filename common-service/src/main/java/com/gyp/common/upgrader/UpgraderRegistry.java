package com.gyp.common.upgrader;

import java.util.List;

import com.gyp.common.enums.version.Version;
import com.gyp.common.exceptions.DataUpgraderException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class UpgraderRegistry {
	private final List<DataUpgrader> dataUpgraderList;

	public void runAllUpgrader(Version currentAppVersion) {
		for(DataUpgrader upgrader : dataUpgraderList) {
			if(shouldRunMigration(upgrader, currentAppVersion)) {
				try {
					if(upgrader instanceof BaseUpgrader baseUpgrader) {
						baseUpgrader.doMigrate();
					}
				} catch(DataUpgraderException e) {
					log.info("Error:", e);
				}
			}
		}
	}

	private boolean shouldRunMigration(DataUpgrader upgrader, Version currentVersion) {
		Version upgraderVersion = upgrader.getVersion();
		return compareVersions(currentVersion, upgraderVersion) > 0;
	}

	private int compareVersions(Version currentVersion, Version upgraderVersion) {
		if(currentVersion.isOlderThan(upgraderVersion)) {
			return -1;
		}
		return 1;
	}
}

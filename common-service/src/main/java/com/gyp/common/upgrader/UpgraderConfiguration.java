package com.gyp.common.upgrader;

import com.gyp.common.enums.version.Version;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UpgraderConfiguration implements InitializingBean {
	private final UpgraderRegistry upgraderRegistry;

	@Override
	public void afterPropertiesSet() {
		upgraderRegistry.runAllUpgrader(Version.V1_0_0);
	}
}

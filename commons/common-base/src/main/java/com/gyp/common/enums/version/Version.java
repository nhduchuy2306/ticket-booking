package com.gyp.common.enums.version;

public enum Version {
	V1_0_0("1.0.0"),
	V1_0_1("1.0.1"),
	V1_0_2("1.0.2"),
	V2_0_0("2.0.0");

	private final String version;

	Version(String version) {
		this.version = version;
	}

	public String getVersion() {
		return version;
	}

	public boolean isNewerThan(Version other) {
		return compareToOther(other) > 0;
	}

	public boolean isOlderThan(Version other) {
		return compareToOther(other) < 0;
	}

	public int compareToOther(Version other) {
		String[] thisParts = version.split("\\.");
		String[] otherParts = other.version.split("\\.");

		for(int i = 0; i < Math.max(thisParts.length, otherParts.length); i++) {
			int thisPart = (i < thisParts.length) ? Integer.parseInt(thisParts[i]) : 0;
			int otherPart = (i < otherParts.length) ? Integer.parseInt(otherParts[i]) : 0;

			if(thisPart < otherPart) {
				return -1;
			}
			if(thisPart > otherPart) {
				return 1;
			}
		}
		return 0;
	}
}

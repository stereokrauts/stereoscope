package main.protection;

/**
 * This class provides a public function to compare two version
 * strings.
 * @author theide
 *
 */
public final class VersionComparator {
	private final String currentVersion;
	private final String newVersion;
	private int currentVersionPoint;
	private int newVersionPoint;

	protected VersionComparator(final String currentVer, final String newVer) {
		this.currentVersion = currentVer;
		this.newVersion = newVer;
	}
	
	protected boolean isNewer() {
		this.findPointsInVersions();
		if (this.oneVersionOnlyHasMajorComponent()) {
			return this.compareOnlyMajorVersion();
		}
		return this.compareAllSubversions();
	}

	private void findPointsInVersions() {
		this.currentVersionPoint = this.currentVersion.indexOf(".");
		this.newVersionPoint = this.newVersion.indexOf(".");
	}

	private boolean compareAllSubversions() {
		final String[] currentParts = this.currentVersion.split("\\.");
		final String[] newParts = this.newVersion.split("\\.");
		for (int i = 0; i < currentParts.length; i++) {
			if (newParts.length <= i) {
				return false;
			}
			if (Integer.parseInt(currentParts[i]) < Integer.parseInt(newParts[i])) {
				return true;
			} else if (Integer.parseInt(currentParts[i]) > Integer.parseInt(newParts[i])) {
				return false;
			}
		}
		if (newParts.length > currentParts.length) {
			return true;
		}
		return false;
	}

	private boolean oneVersionOnlyHasMajorComponent() {
		return this.currentVersionPoint == -1 || this.newVersionPoint == -1;
	}

	private boolean compareOnlyMajorVersion() {
		final Integer currentMajorVersion = this.extractMajorVersion(this.currentVersion, this.currentVersionPoint);
		final Integer newMajorVersion = this.extractMajorVersion(this.newVersion, this.newVersionPoint);
		return (currentMajorVersion < newMajorVersion || ((currentMajorVersion == newMajorVersion) && this.newVersionHasSubversion()));
	}

	private boolean newVersionHasSubversion() {
		return (this.newVersionPoint != -1);
	}

	private int extractMajorVersion(final String versionString, final int versionPoint) {
		return Integer.parseInt(versionString.substring(0, (versionPoint != -1) ? versionPoint : versionString.length()));
	}
	
	public static boolean isNewerVersion(final String currentVersion, final String newVersion) {
		return (new VersionComparator(currentVersion, newVersion).isNewer());
		
	}
}

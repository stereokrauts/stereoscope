package com.stereokrauts.lib.midi.internal;

/**
 * This class provides general information about the environemnt stereoscope
 * runs under.
 * @author Tobias Heide &lt;tobi@s-hei.de&gt;
 *
 */
public final class EnvironmentInformation {
	
	/**
	 * Only static methods allowed.
	 */
	private EnvironmentInformation() { }
	
	/**
	 * @return true if the operating system stereoscope is running on is
	 * mac os x.
	 */
	public static boolean isMacOsX() {
		final String osName = System.getProperty("os.name").toLowerCase();
		return osName.startsWith("mac os x");
	}
}

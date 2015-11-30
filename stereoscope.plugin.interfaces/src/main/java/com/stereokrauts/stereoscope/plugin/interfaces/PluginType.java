package com.stereokrauts.stereoscope.plugin.interfaces;

/**
 * Enumeration class representing all the possible plugins.
 * 
 * @author Tobias Heide <tobi@s-hei.de>
 */
public final class PluginType {
	/**
	 * A mixer plugin uses this enum instance.
	 */
	public static final PluginType MIXER_PLUGIN = new PluginType(1);
	
	/**
	 * A unique number for each type.
	 */
	private final int type;
	/**
	 * Create a new enum instance.
	 * @param myType type of this instance.
	 */
	private PluginType(final int myType) {
		this.type = myType;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + this.type;
		return result;
	}
	
	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (this.getClass() != obj.getClass()) {
			return false;
		}
		final PluginType other = (PluginType) obj;
		if (this.type != other.type) {
			return false;
		}
		return true;
	}
	
	
}

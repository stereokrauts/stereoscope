package model.surface.touchosc;

/**
 * This class is a internal representation of a TouchOSC client
 * that was found by stereoscope in the network.
 * @author theide
 *
 */
public final class BonjourAdvertisedTouchOscInstance {
	/**
	 * 
	 */
	private final BonjourTouchOscHelper touchOscBonjour;
	private final String ipAddress;
	private final int port;
	private String name;

	protected BonjourAdvertisedTouchOscInstance(
			final BonjourTouchOscHelper myTouchOscBonjour, final String ip, final int myPort) {
		this.touchOscBonjour = myTouchOscBonjour;
		this.ipAddress = ip;
		this.port = myPort;
	}

	protected BonjourAdvertisedTouchOscInstance(
			final BonjourTouchOscHelper myTouchOscBonjour, final String ip, final int myPort,
			final String myName) {
		this.touchOscBonjour = myTouchOscBonjour;
		this.ipAddress = ip;
		this.port = myPort;
		this.name = myName;
	}

	public String getIpAddress() {
		return this.ipAddress;
	}

	public int getPort() {
		return this.port;
	}

	@Override
	public String toString() {
		if (this.name != null) {
			return "AUTO: " + this.ipAddress + ":" + this.port + " (" + this.name + ")";
		}
		return "AUTO: " + this.ipAddress + ":" + this.port;
	}

	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + this.getOuterType().hashCode();
		result = prime * result
				+ ((this.ipAddress == null) ? 0 : this.ipAddress.hashCode());
		result = prime * result + this.port;
		return result;
	}

	
	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null || this.getClass() != obj.getClass()) {
			return false;
		}
		final BonjourAdvertisedTouchOscInstance other = (BonjourAdvertisedTouchOscInstance) obj;
		if (!this.getOuterType().equals(other.getOuterType())) {
			return false;
		}
		if (this.ipAddress == null) {
			if (other.ipAddress != null) {
				return false;
			}
		} else if (!this.ipAddress.equals(other.ipAddress)) {
			return false;
		}
		if (this.port != other.port) {
			return false;
		}
		return true;
	}

	private BonjourTouchOscHelper getOuterType() {
		return this.touchOscBonjour;
	}
}
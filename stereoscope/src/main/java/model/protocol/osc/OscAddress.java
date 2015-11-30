package model.protocol.osc;

/**
 * Represets a OSC address, which is used to identify
 * a certain communication endpoint.
 * @author th
 *
 */
public final class OscAddress {
	private final String addressString;

	public OscAddress(final String addressString) {
		this.addressString = addressString;
	}

	String getAddressString() {
		return addressString;
	}

	@Override
	public String toString() {
		return String.format("[OscAddress: addressString=%s]", addressString);
	}
}

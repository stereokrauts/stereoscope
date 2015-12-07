package model.protocol.osc;

/**
 * Helper class to perform certain operations on an {@link OscAddress}-Object.
 * @author th
 *
 */
public final class OscAddressUtil {
	private OscAddressUtil() { }

	public static OscAddress create(final String addressString) {
		return new OscAddress(addressString);
	}

	public static String stringify(final OscAddress address) {
		return address.getAddressString();
	}
}

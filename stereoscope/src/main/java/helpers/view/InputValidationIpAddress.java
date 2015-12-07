package helpers.view;

/**
 * This class handles all input validations for the ip addresses.
 * @author theide
 *
 */
public final class InputValidationIpAddress {
	private static final int IP_MIN_OCTET_VALUE = 0;
	private static final int IP_MAX_OCTET_VALUE = 255;
	private static final int IP_NUMBER_OF_OCTETS = 4;
	
	private InputValidationIpAddress() { }
	
	/**
	 * Checks whether the given IP is valid.
	 * @param sip The IP string
	 * @return true if it is valued, false if not.
	 */
	public static boolean validateIpAddress(final String sip) {
		if (sip == null) {
			return false;
		}
        final String[] parts = sip.split("\\.");
        if (parts.length != IP_NUMBER_OF_OCTETS) {
        	return false;
        }
        for (final String s : parts) {
            final int i = Integer.parseInt(s);
            if (i < IP_MIN_OCTET_VALUE || i > IP_MAX_OCTET_VALUE) {
                return false;
            }
        }
        return true;
    }
}

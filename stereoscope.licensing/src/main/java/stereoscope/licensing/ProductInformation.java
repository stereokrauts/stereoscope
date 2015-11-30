package stereoscope.licensing;


/**
 * This is a place for general product information that
 * is being used throughout the application.
 * @author Tobias Heide &lt;tobi@s-hei.de&gt;
 *
 */
public final class ProductInformation {
	/**
	 * The name of the product.
	 */
	public static final String PRODUCT_NAME = "stereoscope";

	/**
	 * This is the version number as it should be compared in the Update check.
	 * It must be ever-increasing.
	 */
	public static final String PRODUCT_VERSION = "3.0.0-SNAPSHOT-cb6185";

	public static String getMajorVersion() {
		return getVersionComponent(PRODUCT_VERSION, 0);
	}

	public static String getMinorVersion() {
		return getVersionComponent(PRODUCT_VERSION, 1);
	}

	public static String getBugfixVersion() {
		return getVersionComponent(PRODUCT_VERSION, 2);
	}

	private static String getVersionComponent(final String productVersion, final int i) {
		final String[] chunks = productVersion.split("\\.");
		return chunks[i];
	}


	/**
	 * Enable developer functions? (Detailed logging)
	 */
	public static final boolean IS_DEVELOPER_VERSION = false;

	/**
	 * The name that shall be displayed to the user.
	 */
	public static final String PRODUCT_DISPLAY_NAME = "Stereoscope, " + PRODUCT_VERSION;

	/**
	 * Link to the Stereokrauts Webshop.
	 */
	public static final String WEBSHOP_LINK = "http://www.stereokrauts.com/shop";

	/**
	 * Link to the Stereokrauts License Manager.
	 */
	public static final String LICENSES_LINK = "http://www.stereokrauts.com/licenses";

	/**
	 * Link to the Stereokrauts Homepage.
	 */
	public static final String HOMEPAGE_LINK = "http://www.stereokrauts.com/";

	/**
	 * Update checker URL.
	 */
	public static final String UPDATE_CHECKER_URL = "http://www.stereokrauts.com/update-check";

	/**
	 * Day on which this program expires.
	 */
	public static final int EXPIRATION_DAY = 31;
	/**
	 * Month on which this program expires.
	 */
	public static final int EXPIRATION_MONTH = 12;
	/**
	 * Year on which this program expires.
	 */
	public static final int EXPIRATION_YEAR = 5000;

	/**
	 * Logging details of core message dispatcher.
	 */
	public static final boolean CORE_MESSAGE_DISPATCHER_VERBOSE = false;

	/**
	 * Logging details of MIDI messages being on the bus.
	 */
	public static final boolean MIDI_VERBOSE = false;

	public static final String TOUCHOSC_FRONTEND_FILENAME = "maincontrol_stereoscope3-ipad-64ch.touchosc";

	/** Protected default constructor. */
	private ProductInformation() {
		throw new UnsupportedOperationException("not instanziable");
	}
}

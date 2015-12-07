package model;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;

import javax.swing.JOptionPane;

import org.apache.commons.io.IOUtils;

import stereoscope.licensing.ProductInformation;

import com.stereokrauts.lib.commons.ApplicationPath;
import com.stereokrauts.lib.logging.SLogger;
import com.stereokrauts.lib.logging.StereoscopeLogManager;

/**
 * This class contains and manages all application global properties.
 * Currently it holds information about the last mixer setup (midi
 * connections, mixer type, client ip...) and a field which contains
 * a number indicating whether this application has been started for
 * the first time.
 * @author Tobias Heide &lt;tobi@s-hei.de&gt;
 *
 */
public final class ApplicationProperties {
	/**
	 * The logger instance for this object.
	 */
	private static final SLogger LOGGER = StereoscopeLogManager.getLogger("model");

	private final ApplicationPath appPath = new ApplicationPath();

	/**
	 * The last started version.
	 */
	private static final String PROPERTY_LAST_STARTED_VERSION = "lastVersion";
	/**
	 * Has the user allowed update checks?
	 */
	private static final String PROPERTY_MAY_CHECK_FOR_UPDATES = "updateCheck";

	/**
	 * The object containing and managing the properties file.
	 */
	private Properties props;

	private final String propsFilename = "properties.txt";

	public void setMayCheckForUpdates(final boolean mayCheck) {
		this.props.setProperty(PROPERTY_MAY_CHECK_FOR_UPDATES, mayCheck ? "true" : "false");
	}

	public boolean getMayCheckForUpdates() {
		final String prop = this.props.getProperty(PROPERTY_MAY_CHECK_FOR_UPDATES);
		return (prop != null) && prop.equals("true");
	}

	/**
	 * Updates the last started version flag on this properties file.
	 */
	public void updateLastStartedVersion() {
		this.props.setProperty(PROPERTY_LAST_STARTED_VERSION, 
				ProductInformation.PRODUCT_VERSION);
	}

	/**
	 * @return true if the last started version was not this version of
	 * stereoscope.
	 */
	public boolean isNewVersionOfStereoscope() {
		return (this.props.getProperty(PROPERTY_LAST_STARTED_VERSION) == null)
				|| !this.props.getProperty(PROPERTY_LAST_STARTED_VERSION).equals(
						ProductInformation.PRODUCT_VERSION);
	}

	/**
	 * Create a new instance of the ApplicationProperties object. This class
	 * uses the singleton pattern!
	 */
	private ApplicationProperties() {
		this.readProperties();
	}

	/**
	 * Read the properties from the properties file.
	 */
	private synchronized void readProperties() {
		try {
			final InputStream propInFile = appPath.readFile(this.propsFilename);
			this.props = new Properties();
			this.props.loadFromXML(propInFile);
		} catch (final Exception e) {
			/* this is not critical */
			LOGGER.info("No properties.txt found yet - creating...");
			this.props = new Properties();
		}
	}

	/**
	 * Save the currently set properties to the properties file.
	 */
	public synchronized void saveProperties() {
		InputStream contents = null;
		ByteArrayOutputStream propOutFile = null;
		try {
			propOutFile = new ByteArrayOutputStream();
			this.props.storeToXML(propOutFile, "oscRemote");
			contents = new ByteArrayInputStream(propOutFile.toByteArray());
			appPath.storeFile(propsFilename, contents);
		} catch (final Exception e) {
			/* not critical, but inform user. */
			JOptionPane.showMessageDialog(null,
					"Could not write your settings to " + this.propsFilename 
					+ ". This is not critical, but you will have to make"
					+ " these settings again at next start.",
					"Could not save settings",
					JOptionPane.WARNING_MESSAGE);
			LOGGER.log(Level.WARNING, "Could not write to properties file "
					+ this.propsFilename + " - warned the user.", e);
		} finally {
			IOUtils.closeQuietly(propOutFile);
			IOUtils.closeQuietly(contents);
		}
	}

	/**
	 * Contains the single instance of this object.
	 */
	private static ApplicationProperties instance;

	/**
	 * @return The singleton of this object.
	 */
	public static synchronized ApplicationProperties getInstance() {
		if (instance == null) {
			instance = new ApplicationProperties();
		}
		return instance;
	}

}

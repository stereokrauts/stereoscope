package main.protection;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.logging.Level;

import stereoscope.licensing.ProductInformation;

import com.stereokrauts.lib.logging.SLogger;
import com.stereokrauts.lib.logging.StereoscopeLogManager;

/**
 * This class implements an update check, which downloads a predefined
 * file from the Stereokrauts webserver and checks it's contents for
 * a newer version than is installed.
 * @author theide
 *
 */
public final class UpdateCheck {
	private static final SLogger LOG = StereoscopeLogManager.getLogger("update-check");
	
	private boolean newerVersionAvailable = false;
	private String newVersionNumber;
	private String releaseNotes = "";

	public UpdateCheck() {
		try {
			final URL checker = new URL(ProductInformation.UPDATE_CHECKER_URL + "?productName=" + ProductInformation.PRODUCT_NAME + "&productVersion=" + ProductInformation.PRODUCT_VERSION);
			final BufferedReader checkResult = new BufferedReader(new InputStreamReader(checker.openStream()));
			
			final String latestVersion = checkResult.readLine().replaceAll("###", "").replaceAll(" ", "");
			
			if (VersionComparator.isNewerVersion(ProductInformation.PRODUCT_VERSION, latestVersion)) {
				this.newerVersionAvailable = true;
				this.newVersionNumber = latestVersion;
				final String line = "";
				while (checkResult.ready() && line.indexOf("###") == -1) {
					this.releaseNotes += checkResult.readLine() + "\n";
				}
			}
			checkResult.close();
		} catch (final Exception e) {
			LOG.log(Level.WARNING, "Could not check for Update", e);
		}
	}
	
	public boolean isNewerVersionAvailable() {
		return this.newerVersionAvailable;
	}
	
	public String getReleaseNotes() {
		return this.releaseNotes;
	}
	
	public String getNewVersionNumber() {
		return this.newVersionNumber;
	}
}

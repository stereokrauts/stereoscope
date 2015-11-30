package model.surface.touchosc;

import java.io.IOException;

import javax.jmdns.JmDNS;
import javax.jmdns.ServiceInfo;

import org.eclipse.jetty.server.Server;

import com.stereokrauts.lib.logging.SLogger;
import com.stereokrauts.lib.logging.StereoscopeLogManager;

/**
 * This class uploads the frontend to a TouchOSC client.
 * @author theide
 *
 */
public final class TouchOscFrontendUploader {
	private static final SLogger LOG = StereoscopeLogManager
			.getLogger("touchosc-webserver");

	public static final int TOUCHOSC_PORT = 17500;
	public static final int LOCAL_UPLOAD_PORT = TOUCHOSC_PORT + 1;

	private Server server;
	private JmDNS jm;
	private static TouchOscFrontendUploader instance;

	private TouchOscFrontendUploader() throws Exception {
		this.advertiseWebserver();
		this.initializeWebserver();
	}

	/**
	 * @throws UnknownHostException
	 * @throws IOException
	 */
	private void advertiseWebserver() throws IOException {
		final ServiceInfo info = ServiceInfo.create("_touchosceditor._tcp.local", "Stereoscope", LOCAL_UPLOAD_PORT, "");

		this.jm = JmDNS.create();
		this.jm.registerService(info);
	}

	/**
	 * @throws IOException
	 */
	private void initializeWebserver() throws IOException {
		this.server = new Server(LOCAL_UPLOAD_PORT);
		this.server.setHandler(new TouchOscFrontendUploaderWebserver());

		try {
			this.server.start();
			this.server.join();
		} catch (final Exception e) {
			LOG.error("Could not start Jetty Webserver for Frontend Upload: " + e.getMessage());
		}
	}

	public void shutdown() throws IOException {
		try {
			this.jm.unregisterAllServices();
			this.jm.close();
		} catch (final Exception e) {
			LOG.error("Could not shutdown Bonjour services for Frontend uploader: " + e.getMessage());
		}
		try {
			this.server.stop();
		} catch (final Exception e) {
			LOG.error("Could not stop Jetty Webserver for Frontend Upload: " + e.getMessage());
		}
	}

	public static TouchOscFrontendUploader getInstance() throws Exception {
		if (instance == null) {
			instance = new TouchOscFrontendUploader();
		}
		return instance;
	}

}

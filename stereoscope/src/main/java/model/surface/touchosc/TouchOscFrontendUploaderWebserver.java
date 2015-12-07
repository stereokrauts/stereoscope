package model.surface.touchosc;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.ByteBuffer;
import java.util.logging.Level;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import stereoscope.licensing.ProductInformation;

import com.stereokrauts.lib.logging.SLogger;
import com.stereokrauts.lib.logging.StereoscopeLogManager;

/**
 * This is the real webserver doing the hard work of uploading the frontend file
 * to TouchOSC clients.
 * 
 * @author theide
 * 
 */
class TouchOscFrontendUploaderWebserver extends AbstractHandler {
	private static final SLogger LOG = StereoscopeLogManager.getLogger("touchosc-webserver");

	private ByteBuffer frontendFileContents;
	private HttpServletResponse frontendRequestingClient;

	/**
	 * @throws IOException
	 */
	private void sendResponseData() throws IOException {
		final byte[] response = new byte[frontendFileContents.position()];
		frontendFileContents.flip();
		frontendFileContents.get(response);
		frontendRequestingClient.getOutputStream().write(response);
		// frontendRequestingClient.getOutputStream().write('\n');

		LOG.info("Uploading frontend - data sent");
	}

	/**
	 * @throws IOException
	 */
	private void sendResponseHeaders() throws IOException {
		frontendRequestingClient.setContentType("application/touchosc");
		frontendRequestingClient.setStatus(HttpServletResponse.SC_OK);
		frontendRequestingClient.setDateHeader("Date", System.currentTimeMillis());
		frontendRequestingClient.setContentLength(frontendFileContents.position());
		frontendRequestingClient.addHeader("Content-Disposition", "attachment; filename=\"" + ProductInformation.TOUCHOSC_FRONTEND_FILENAME
				+ "\"");
		LOG.info("Uploading frontend - headers sent (filesize=" + frontendFileContents.position() + ")");
	}

	private void openFrontendFile() throws IOException {
		final InputStream frontendFileStream = FileLocator.resolve(
				new URL("platform:/plugin/stereoscope/src/main/resources/" + ProductInformation.TOUCHOSC_FRONTEND_FILENAME)).openStream();

		final ZipInputStream frontendFile = new ZipInputStream(frontendFileStream);
		final ZipEntry xmlFrontendFile = frontendFile.getNextEntry();
		if (xmlFrontendFile == null) {
			throw new RuntimeException("Could not uncompress frontend file!");
		} else {
			frontendFileContents = ByteBuffer.allocate(1024 * 1024);
			while (frontendFile.available() > 0) {
				final byte[] c = new byte[1];
				frontendFile.read(c);
				frontendFileContents.put(c);
			}
			frontendFile.close();
			frontendFileStream.close();
		}
	}

	@Override
	public void handle(final String arg0, final Request baseRequest, final HttpServletRequest request, final HttpServletResponse response)
			throws IOException, ServletException {
		frontendRequestingClient = response;
		LOG.info("Got frontend upload request: " + request);
		try {
			this.openFrontendFile();
			this.sendResponseHeaders();
			this.sendResponseData();
			baseRequest.setHandled(true);
		} catch (final Exception e) {
			LOG.log(Level.WARNING, "Failed to upload frontend", e);
		}
		LOG.info("Sucessfully handled frontend upload request - returning.");
	}
}

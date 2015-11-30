package model.surface.touchosc;

import static org.junit.Assert.assertNotNull;

import java.net.URL;

import org.junit.Ignore;

public final class TouchOscFrontendUploaderTest {
	private final Object syncLock = new Object();
	private TouchOscFrontendUploader upl;

	@Ignore
	public final void frontendUploader() throws Exception {
		final Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					upl = TouchOscFrontendUploader.getInstance();
					syncLock.notifyAll();
				} catch (final Exception e) {
					e.printStackTrace();
				}
			}
		});
		t.start();
		syncLock.wait();
		final URL webserver = new URL("http://localhost:" + TouchOscFrontendUploader.LOCAL_UPLOAD_PORT);
		assertNotNull(webserver.getContent());
		upl.shutdown();
	}
}

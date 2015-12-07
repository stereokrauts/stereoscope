package main;

import java.text.DateFormat;
import java.util.Calendar;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;

import com.stereokrauts.lib.logging.StereoscopeLogManager;

import commands.shutdown.ShutdownApplicationCommand;
import stereoscope.licensing.ProductInformation;

/**
 * This is the bundle activator of the Stereocope CORE. You found it. Welcome.
 * @author theide
 *
 */
public final class Application implements BundleActivator {
	public static final String PLUGIN_NAME = "stereoscope";

	private static BundleContext context;

	@Override
	public void start(final BundleContext context) throws Exception {
		StereoscopeLogManager.getLogger(Application.class).info("In Application Activator of stereoscope");
		Application.setContext(context);
	}
	
	@Override
	public void stop(final BundleContext context) throws Exception {
		(new ShutdownApplicationCommand()).execute();		
	}

	public static BundleContext getContext() {
		return context;
	}

	private static void setContext(final BundleContext context) {
		Application.context = context;
	}


}

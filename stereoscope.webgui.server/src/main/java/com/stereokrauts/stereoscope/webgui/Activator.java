package com.stereokrauts.stereoscope.webgui;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class Activator implements BundleActivator {
	private static BundleContext context;

	@Override
	public void start(BundleContext context) throws Exception {
		Activator.context = context;
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		if (context == Activator.context) {
			Activator.context = null;
		}
	}
	
	public static BundleContext getContext() {
		return Activator.context;
	}

}

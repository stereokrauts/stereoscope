package com.stereokrauts.stereoscope.plugin.interfaces;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public final class Activator implements BundleActivator {
	private static BundleContext context;

	@Override
	public void start(final BundleContext context) throws Exception {
		Activator.context = context;
	}

	@Override
	public void stop(final BundleContext context) throws Exception {
	}

	public static BundleContext getContext() {
		return context;
	}
}

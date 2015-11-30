package com.stereokrauts.stereoscope.plugin;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import com.stereokrauts.stereoscope.plugin.manager.MixerPluginManager;

public final class Activator implements BundleActivator {
	private static BundleContext context;

	@Override
	public void start(final BundleContext context) throws Exception {
		Activator.context = context;
		MixerPluginManager.getInstance();
	}

	@Override
	public void stop(final BundleContext context) throws Exception {
	}

	public static BundleContext getContext() {
		return context;
	}
}

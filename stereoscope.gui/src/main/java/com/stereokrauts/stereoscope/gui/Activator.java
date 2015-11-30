package com.stereokrauts.stereoscope.gui;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import com.stereokrauts.lib.logging.StereoscopeLogManager;
import com.stereokrauts.stereoscope.gui.log.EclipseLoggingAdapter;

/**
 * The activator of the GUI.
 * @author th
 *
 */
public final class Activator extends AbstractUIPlugin {
	private static Activator sharedInstance;

	public Activator() {
		if (sharedInstance == null) {
			sharedInstance = this;
		}
	}
	
	@Override
	public void start(final BundleContext context) throws Exception {
		super.start(context);
		StereoscopeLogManager.getInstance().getViewProvider().registerView(new EclipseLoggingAdapter());
	}
	
	public static Activator getDefault() {
		return sharedInstance;
	}

}

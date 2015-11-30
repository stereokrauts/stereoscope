package com.stereokrauts.stereoscope.gui;

import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;

import com.stereokrauts.lib.logging.SLogger;
import com.stereokrauts.lib.logging.StereoscopeLogManager;
import com.stereokrauts.stereoscope.plugin.manager.*;
import com.stereokrauts.stereoscope.gui.startup.StartupAdvisor;

/**
 * The activator class controls the plug-in life cycle.
 */
public final class Application implements IApplication {
	private static final SLogger LOG = StereoscopeLogManager.getLogger(Application.class);

	// The plug-in ID
	public static final String PLUGIN_ID = "stereoscope.gui"; //$NON-NLS-1$

	public Application() {
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	@Override
	public Object start(final IApplicationContext context) throws Exception {
		LOG.info("In Activator of stereoscope.gui");
		MixerPluginManager.getInstance();
		Thread.currentThread().setName("Stereoscope SWT GUI Thread");
		final Display display = PlatformUI.createDisplay();
		try {
			final int code = PlatformUI.createAndRunWorkbench(display,
					new StartupAdvisor());

			// exit the application with an appropriate return code
			return code == PlatformUI.RETURN_RESTART
					? EXIT_RESTART
							: EXIT_OK;
		} finally {
			if (display != null) {
				display.dispose();
			}
		}

	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	@Override
	public void stop() {
		final IWorkbench workbench = PlatformUI.getWorkbench();
		if (workbench == null) {
			return;
		}
		final Display display = workbench.getDisplay();
		display.syncExec(new Runnable() {
			@Override
			public void run() {
				if (!display.isDisposed()) {
					workbench.close();
				}
			}
		});
	}

}

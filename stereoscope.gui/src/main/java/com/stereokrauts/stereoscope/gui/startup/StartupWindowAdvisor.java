package com.stereokrauts.stereoscope.gui.startup;

import java.util.logging.Level;

import model.surface.touchosc.BonjourTouchOscHelper;
import model.surface.touchosc.TouchOscFrontendUploader;
import stereoscope.licensing.ProductInformation;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.swt.graphics.Point;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;

import com.stereokrauts.lib.logging.SLogger;
import com.stereokrauts.lib.logging.StereoscopeLogManager;
import com.stereokrauts.stereoscope.controller.Controller;

/**
 * This class defines the initiali layout of the stereoscope main window.
 * @author th
 *
 */
public final class StartupWindowAdvisor extends WorkbenchWindowAdvisor {
	private static final int STARTUP_WINDOW_HEIGHT = 720;
	private static final int STARTUP_WINDOW_WIDTH = 1000;
	private static final SLogger LOGGER = StereoscopeLogManager.getLogger(StartupWindowAdvisor.class);

	@SuppressWarnings("unused")
	private Controller controller;

	public StartupWindowAdvisor(final IWorkbenchWindowConfigurer configurer) {
		super(configurer);
	}

	@Override
	public void preWindowOpen() {
		super.preWindowOpen();
		this.controller = new Controller();
		final IWorkbenchWindowConfigurer configurer = this.getWindowConfigurer();
		configurer.setTitle(String.format("Stereoscope Version %s.%s.%s", ProductInformation.getMajorVersion(), ProductInformation.getMinorVersion(), ProductInformation.getBugfixVersion()));
		configurer.setInitialSize(new Point(STARTUP_WINDOW_WIDTH, STARTUP_WINDOW_HEIGHT));
		configurer.setShowMenuBar(true);
		configurer.setShowStatusLine(true);
		configurer.setShowProgressIndicator(true);
	}

	@Override
	public void postWindowOpen() {
		super.postWindowOpen();
		this.startBonjourSubsystem();
		this.startTouchOscWebserver();
	}

	private void startBonjourSubsystem() {
		final Job startJob = new Job("Starting Bonjour subsystem ...") {
			@Override
			protected IStatus run(final IProgressMonitor monitor) {
				try {
					BonjourTouchOscHelper.getInstance();
				} catch (final Exception e) {
					LOGGER.log(Level.WARNING, "Could not start Bonjour", e);
					return Status.CANCEL_STATUS;
				}
				return Status.OK_STATUS;
			}
		};
		startJob.schedule();
	}

	private void startTouchOscWebserver() {
		final Job startJob = new Job("Starting TouchOSC Frontend Uploader ...") {
			@Override
			protected IStatus run(final IProgressMonitor monitor) {
				new Thread(new Runnable() {
					@Override
					public void run() {
						try {
							TouchOscFrontendUploader.getInstance();
						} catch (final Exception e) {
							LOGGER.log(Level.WARNING, "Could not start TouchOSC FrontendUploader", e);
						}
					}
				}).start();
				return Status.OK_STATUS;
			}
		};
		startJob.schedule();
	}
}

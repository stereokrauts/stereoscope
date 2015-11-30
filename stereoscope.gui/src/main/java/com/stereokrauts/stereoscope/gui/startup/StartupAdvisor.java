package com.stereokrauts.stereoscope.gui.startup;

import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchAdvisor;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;

/**
 * This class returns an object that knows how to setup the
 * workbench window.
 * @author th
 *
 */
public final class StartupAdvisor extends WorkbenchAdvisor {
	
	@Override
	public WorkbenchWindowAdvisor createWorkbenchWindowAdvisor(final IWorkbenchWindowConfigurer configurer) {
		return new StartupWindowAdvisor(configurer);
	}

	@Override
	public String getInitialWindowPerspectiveId() {
		return "stereoscope.gui.perspectives.startup";
	}

}

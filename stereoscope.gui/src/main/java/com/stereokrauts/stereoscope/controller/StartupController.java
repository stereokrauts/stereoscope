package com.stereokrauts.stereoscope.controller;

import java.io.File;
import java.util.logging.Level;

import model.IModel;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

import com.stereokrauts.lib.logging.SLogger;
import com.stereokrauts.lib.logging.StereoscopeLogManager;
import com.stereokrauts.stereoscope.controller.interfaces.IController;
import com.stereokrauts.stereoscope.gui.startup.StartupView;
import com.stereokrauts.stereoscope.plugin.gui.IViewContext;
import com.stereokrauts.stereoscope.plugin.guihelper.MixerPluginImageAssociation;
import com.stereokrauts.stereoscope.plugin.guihelper.MixerPluginImageStore;

import commands.AddMixerToDocumentCommand;
import commands.CommandExecutor;
import commands.document.NewDocumentCommand;

/**
 * This is the controller that is in charge of the startup/greeting
 * view of Stereoscope.
 * @author th
 *
 */
public final class StartupController implements IController<StartupView, IModel> {
	private StartupView view;
	private IModel model;
	private boolean messageWasDisplayed = false;

	private static final SLogger LOG = StereoscopeLogManager.getLogger(StartupController.class);

	public void createNewDocumentFromStartupView(final String pluginId) {
		NewDocumentCommand newDoc;
		try {
			final CommandExecutor ex = this.model.getExecutor();
			newDoc = new NewDocumentCommand(this.model);
			ex.executeCommand(newDoc);
			newDoc.getReturnValue().getApplicationContextForPlugin().setViewContext(new IViewContext() {
				@Override
				public Shell getRootShell() {
					return PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
				}
			});
			final AddMixerToDocumentCommand addMix = new AddMixerToDocumentCommand(newDoc.getReturnValue(), pluginId);
			ex.executeCommand(addMix);
		} catch (final Exception e) {
			LOG.log(Level.SEVERE, "Could not create new document.", e);
			MessageDialog.openError(getView().getSite().getShell(), "Could not create new document", "The initialisation failed (see error log for details - " + e.getMessage());
		}
	}

	@Override
	public StartupView getView() {
		return this.view;
	}

	@Override
	public void setView(final StartupView view) {
		this.view = view;
		view.setController(this);

		for (final MixerPluginImageAssociation assc : MixerPluginImageStore.getInstance().getAllEntries()) {
			view.addMixerButton(assc);
		}
	}

	@Override
	public void setModel(final IModel model) {
		this.model = model;
		this.openFileIfOptionPresent();
	}


	private static final String OPTION_LOAD_FILE = "loadFile";
	private void openFileIfOptionPresent() {
		try {
			final String fileName = System.getProperty(OPTION_LOAD_FILE);
			if (fileName != null) {
				this.model.createNewDocumentFromFile(new File(fileName));
			}
		} catch (final Exception e) {
			StereoscopeLogManager.getLogger(this.getClass()).log(Level.SEVERE, "Could not open file ", e);
		}
	}

	@Override
	public IModel getModel() {
		return this.model;
	}

}

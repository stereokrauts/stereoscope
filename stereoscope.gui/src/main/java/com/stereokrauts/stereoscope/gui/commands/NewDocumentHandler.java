package com.stereokrauts.stereoscope.gui.commands;

import java.util.logging.Level;

import model.IModel;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

import com.stereokrauts.lib.logging.SLogger;
import com.stereokrauts.lib.logging.StereoscopeLogManager;
import com.stereokrauts.stereoscope.controller.Controller;
import com.stereokrauts.stereoscope.gui.main.NewDocumentDialog;
import com.stereokrauts.stereoscope.gui.main.NewDocumentDialogResult;
import com.stereokrauts.stereoscope.plugin.gui.IViewContext;
import commands.AddMixerToDocumentCommand;
import commands.CommandExecutor;
import commands.document.NewDocumentCommand;

public final class NewDocumentHandler extends AbstractHandler {
	private static final SLogger LOG = StereoscopeLogManager.getLogger(Controller.class);
	private IModel model;

	@Override
	public Object execute(final ExecutionEvent event) throws ExecutionException {
		final NewDocumentDialog dlg = new NewDocumentDialog(PlatformUI.getWorkbench().getDisplay().getActiveShell(), SWT.NONE);
		final NewDocumentDialogResult result = dlg.open();
		if (result.isClosedUsingOkay()) {
			this.add(result.getPluginId());
		}
		return null;
	}

	private void add(final String pluginId) {
		NewDocumentCommand newDoc;
		try {
			final CommandExecutor ex = this.getModel().getExecutor();
			newDoc = new NewDocumentCommand(this.getModel());
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
		}
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	public IModel getModel() {
		return this.model;
	}

	public void setModel(final IModel model) {
		this.model = model;
	}

}

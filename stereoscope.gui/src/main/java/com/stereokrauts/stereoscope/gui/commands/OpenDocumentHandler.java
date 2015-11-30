package com.stereokrauts.stereoscope.gui.commands;

import java.io.File;

import model.IModel;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.ui.PlatformUI;

public final class OpenDocumentHandler extends AbstractHandler {
	private IModel model;

	@Override
	public Object execute(final ExecutionEvent event) throws ExecutionException {
		final FileDialog dlg = new FileDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell());
		dlg.setFilterExtensions(new String[] {"*.stsc"});
		final String file = dlg.open();
		if (file != null) {
			this.model.createNewDocumentFromFile(new File(file));
		}
		return null;
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

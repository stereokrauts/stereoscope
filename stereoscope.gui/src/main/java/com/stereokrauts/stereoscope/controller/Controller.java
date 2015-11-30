package com.stereokrauts.stereoscope.controller;

import java.util.logging.Level;

import model.Document;
import model.IModel;
import model.IObserveModel;

import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

import com.stereokrauts.lib.logging.SLogger;
import com.stereokrauts.lib.logging.StereoscopeLogManager;
import com.stereokrauts.stereoscope.gui.document.EditableDocument;

/**
 * This is the master controller of everything.
 * @author th
 *
 */
public final class Controller implements IObserveModel {
	private static final SLogger LOG = StereoscopeLogManager.getLogger(Controller.class);

	private IModel model;

	public void setModel(final IModel m) {
		this.model = m;
		this.model.registerObserver(this);
	}

	@Override
	public void newDocumentEvent(final Document doc) {
		LOG.info("New document was created. Displaying editor.");
		try {
			PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().openEditor(new EditableDocument(doc), "stereoscope.controller.document");
		} catch (final PartInitException e) {
			LOG.log(Level.SEVERE, "Could not create editor for Document", e);
		}
	}
}

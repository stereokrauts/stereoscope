package com.stereokrauts.stereoscope.controller;

import java.io.File;
import java.util.logging.Level;

import model.Document;
import model.IObserveDocumentChanges;
import model.bus.Bus;
import model.bus.BusAttendee;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;

import com.stereokrauts.lib.logging.SLogger;
import com.stereokrauts.lib.logging.StereoscopeLogManager;
import com.stereokrauts.stereoscope.controller.interfaces.IController;
import com.stereokrauts.stereoscope.gui.document.DocumentEditor;
import com.stereokrauts.stereoscope.wizards.document.BusElementAddWizard;
import com.stereokrauts.stereoscope.wizards.document.BusElementAddWizardResult;

import commands.document.AddTouchOscSurfaceCommand;
import commands.document.AddWebclientCommand;
import commands.document.CloseDocumentCommand;
import commands.document.RemoveBusAttendeeCommand;

/**
 * This class is responsible for the "glue" between model and the central
 * document display of stereoscope.
 * @author th
 *
 */
public final class DocumentController implements IController<DocumentEditor, Document>, IObserveDocumentChanges {
	private static final SLogger LOG = StereoscopeLogManager.getLogger(DocumentController.class);

	private Document document; 
	private DocumentEditor view;

	public DocumentController() {
	}

	@Override
	public DocumentEditor getView() {
		return this.view;
	}

	@Override
	public void setView(final DocumentEditor view) {
		LOG.info("New view for document controller " + this + ": " + view);
		this.view = view;
		view.setController(this);
	}

	@Override
	public Document getModel() {
		return this.document;
	}

	@Override
	public void setModel(final Document model) {
		this.document = model;
		model.registerObserver(this);
		for (final BusAttendee attendee : model.getMasterBus().getAttendees()) {
			this.busElementAdded(model.getMasterBus(), attendee);
		}
	}

	@Override
	public void busElementAdded(final Bus bus, final BusAttendee attendee) {
		LOG.info("New bus element: " + attendee + " on bus " + bus);
		this.view.newBusElement(bus, attendee);
		view.fireDirty();
	}

	@Override
	public void busElementRemoved(final Bus bus, final BusAttendee attendee) {
		LOG.info("Bus element removed: " + attendee + " on bus " + bus);
		this.view.removeBusElement(bus, attendee);
		view.fireDirty();
	}

	public void newBusElementRequested() {
		final BusElementAddWizard wizard = new BusElementAddWizard();
		final WizardDialog dialog = new WizardDialog(Display.getCurrent().getActiveShell(), wizard);
		if (dialog.open() == Window.OK) {
			if (wizard.getResult().isTouchOSC()) {
				LOG.info("Adding new TouchOSC interface");
				final BusElementAddWizardResult res = wizard.getResult();
				final AddTouchOscSurfaceCommand addCmd = new AddTouchOscSurfaceCommand(this.document, res.getNetworkAddress(), res.getNetworkPort(), res.getServerNetworkPort());
				try {
					this.document.getModel().getExecutor().executeCommand(addCmd);
				} catch (final Exception e) {
					MessageDialog.openError(Display.getDefault().getActiveShell(), "Error adding TouchOSC interface", "See errorlog for details - " + e.getMessage());
					LOG.log(Level.SEVERE, "Could not add new TouchOSC interface", e);
				}
			} else if (wizard.getResult().isWebclient()) {
				LOG.info("Adding new Webclient");
				final AddWebclientCommand addCmd = new AddWebclientCommand(this.document, wizard.getResult().getWebclientPort());
				try {
					this.document.getModel().getExecutor().executeCommand(addCmd);
				} catch (final Exception e) {
					MessageDialog.openError(Display.getDefault().getActiveShell(), "Error adding Webclient interface", "See errorlog for details - " + e.getMessage());
					LOG.log(Level.SEVERE, "Could not add new Webclient interface", e);
				}
			}
		} else {
			LOG.log(Level.INFO, "User aborted SurfaceConnectionSetupDialog");
		}
	}

	public void removeBusElementRequested(final Bus bus, final BusAttendee attendee) {
		LOG.info("Removing bus attendee " + attendee + " from bus " + bus);
		final RemoveBusAttendeeCommand removeCmd = new RemoveBusAttendeeCommand(this.document, bus, attendee);
		try {
			this.document.getModel().getExecutor().executeCommand(removeCmd);
		} catch (final Exception e) {
			LOG.log(Level.SEVERE, "Could not remove bus attendee", e);
		}
	}

	public void displayPropertiesOf(final Bus bus, final BusAttendee attendee) {
		this.view.getPropertiesView().setInput(attendee.getPropertyProvider());
	}

	public void doSave() {
		if (this.document.getFileRepresentation() != null) {
			this.document.save();
		} else {
			this.doSaveAs();
		}
	}

	public void doSaveAs() {
		final FileDialog dlg = new FileDialog(this.view.getSite().getShell(), SWT.SAVE);
		if (this.document.getFileRepresentation() != null) {
			dlg.setFileName(this.document.getFileRepresentation().getAbsolutePath());
		}
		dlg.setFilterExtensions(new String[] {"*.stsc"});
		dlg.setOverwrite(true);
		final String file = dlg.open();
		if (file != null) {
			this.document.setFileRepresentation(new File(file));
			this.document.save();
		}		
	}

	public boolean isDirty() {
		return this.document.isDirty();
	}

	@Override
	public void documentShutdown(final Document document) {
		// ignore 
	}

	public void shutdown() {
		final CloseDocumentCommand closeCommand = new CloseDocumentCommand(this.document);
		try {
			this.document.getModel().getExecutor().executeCommand(closeCommand);
		} catch (final Exception e) {
			LOG.log(Level.SEVERE, "Could not close the document", e);
		}	
	}


}

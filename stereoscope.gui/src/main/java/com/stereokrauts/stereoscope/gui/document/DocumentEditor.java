package com.stereokrauts.stereoscope.gui.document;

import model.bus.Bus;
import model.bus.BusAttendee;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;
import org.eclipse.wb.swt.ResourceManager;

import com.stereokrauts.lib.logging.SLogger;
import com.stereokrauts.lib.logging.StereoscopeLogManager;
import com.stereokrauts.stereoscope.controller.DocumentController;

public final class DocumentEditor extends EditorPart {
	private static final SLogger LOG = StereoscopeLogManager.getLogger(DocumentEditor.class);

	public static final String ID = "com.stereokrauts.stereoscope.gui.document.DocumentView"; //$NON-NLS-1$
	private DocumentController controller;
	private BusView busView;
	private PropertiesView propView;
	private SashForm sashForm;

	public DocumentEditor() {
		this.setTitleImage(ResourceManager.getPluginImage("org.eclipse.ui", "/icons/full/obj16/file_obj.gif"));
		this.setPartName("Document");
	}

	/**
	 * Create contents of the view part.
	 * @param parent
	 */
	@Override
	public void createPartControl(final Composite parent) {
		sashForm = new SashForm(parent, SWT.NONE);

		this.busView = new BusView(sashForm, SWT.NONE);
		this.busView.setController(this.controller);

		this.busView.setBus(this.controller.getModel().getMasterBus());

		this.propView = new PropertiesView(sashForm, SWT.BORDER | SWT.FULL_SELECTION);

		sashForm.setWeights(new int[] { 70, 30 });

		this.createActions();
		this.initializeToolBar();
		this.initializeMenu();
	}

	/**
	 * Create the actions.
	 */
	private void createActions() {
		// Create the actions
	}

	/**
	 * Initialize the toolbar.
	 */
	private void initializeToolBar() {
		@SuppressWarnings("unused")
		final
		IToolBarManager toolbarManager = this.getEditorSite().getActionBars()
		.getToolBarManager();
	}

	/**
	 * Initialize the menu.
	 */
	private void initializeMenu() {
		@SuppressWarnings("unused")
		final
		IMenuManager menuManager = this.getEditorSite().getActionBars()
		.getMenuManager();
	}

	@Override
	public void setFocus() {
		// Set the focus
	}

	@Override
	public void doSave(final IProgressMonitor monitor) {
		this.controller.doSave();
	}

	@Override
	public void doSaveAs() {
		this.controller.doSaveAs();
	}

	// Will be called before createPartControl
	@Override
	public void init(final IEditorSite site, final IEditorInput input)
			throws PartInitException {
		if (!(input instanceof EditableDocument)) {
			throw new RuntimeException("Wrong input");
		}

		final EditableDocument newName = (EditableDocument) input;
		this.controller.setModel(newName.getDocument());
		this.setSite(site);
		this.setInput(input);
	}

	@Override
	public boolean isDirty() {
		return this.controller.isDirty();
	}

	@Override
	public boolean isSaveAsAllowed() {
		return true;
	}

	public void setController(final DocumentController documentController) {
		LOG.info("The following controller registered with us: " + documentController);
		this.controller = documentController;		
	}

	public void newBusElement(final Bus bus, final BusAttendee attendee) {
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				LOG.info("New bus element " + attendee + " on bus " + bus);
				DocumentEditor.this.busView.addBusAttendee(bus, attendee);
				DocumentEditor.this.busView.layout();
				DocumentEditor.this.propView.layout();
				DocumentEditor.this.sashForm.layout();
			}
		});
	}

	public void removeBusElement(final Bus bus, final BusAttendee attendee) {
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				LOG.info("Removing bus element " + attendee + " on bus " + bus);
				DocumentEditor.this.busView.removeBusAttendee(bus, attendee);
			}
		});
	}

	public PropertiesView getPropertiesView() {
		return this.propView;
	}

	@Override
	public void dispose() {
		super.dispose();
		this.controller.shutdown();
	}

	public void fireDirty() {
		firePropertyChange(IEditorPart.PROP_DIRTY);
	}
}
